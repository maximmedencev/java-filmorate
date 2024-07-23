package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InsertException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("userDbStorage")
public class UserDbStorage implements UserRepository {
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) " +
            "VALUES (:email, :login, :name, :birthday)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = :id";
    String SELECT_ALL_FRIENDS_IDS_BY_ID =
            "SELECT user2_id " +
                    "FROM friendships " +
                    "WHERE user1_id = :user1_id";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER_BY_ID = "UPDATE users "
            + "SET email = :email, login = :login, name = :name, birthday = :birthday WHERE id = :id";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = :id";
    private static final String FRIENDSHIP_REQUEST = "INSERT INTO friendships(user1_id, user2_id) " +
            "VALUES (:user1_id, :user2_id)";
    private static final String END_FRIENDSHIP = "DELETE FROM friendships WHERE user1_id = :user1_id AND user2_id = :user2_id";
    private static final String COMMON_FRIENDS =
            "SELECT * FROM users WHERE id IN (SELECT user1_id " +
                    "FROM friendships " +
                    "WHERE user2_id = :user_id AND user1_id IN(" +
                    "SELECT user1_id " +
                    "FROM friendships " +
                    "WHERE user2_id = :friend_id) " +
                    "UNION " +
                    "SELECT user2_id " +
                    "FROM friendships " +
                    "WHERE user1_id = :user_id AND user2_id IN(" +
                    "SELECT user2_id " +
                    "FROM friendships " +
                    "WHERE user1_id = :friend_id))";
    private static final String DELETE_FRIENDSHIPS = "DELETE FROM friendships WHERE user1_id = :user1_id";
    private static final String IS_USER_EXIST_QUERY = "SELECT EXISTS(SELECT FROM users WHERE id = :id)";
    private static final String IS_USER_A_FRIEND =
            "SELECT EXISTS(SELECT FROM friendships WHERE user1_id = :user1_id AND user2_id = :user2_id)";

    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserDbStorage(NamedParameterJdbcOperations jdbc, UserRowMapper userRowMapper) {
        this.jdbc = jdbc;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Boolean isUserAFriend(long user1Id, long user2Id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user1_id", user1Id);
        params.put("user2_id", user2Id);
        return jdbc.queryForObject(IS_USER_A_FRIEND, params, Boolean.class);
    }

    private Boolean isUserExist(long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", userId);
        return jdbc.queryForObject(IS_USER_EXIST_QUERY, params, Boolean.class);
    }

    @Override
    public Optional<User> find(long userId) {
        User user = null;
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", userId);
            user = jdbc.queryForObject(SELECT_USER_BY_ID, params, userRowMapper);
        } catch (DataAccessException ignored) {
        }
        if (user != null) {
            user.setFriendsIds(findAllFriendsIds(userId));
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> allUsers = jdbc.query(SELECT_ALL_USERS, userRowMapper);
        return allUsers.stream()
                .peek(user -> user.setFriendsIds(findAllFriendsIds(user.getId())))
                .toList();
    }

    @Override
    public User create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", java.sql.Date.valueOf(user.getBirthday()));
        jdbc.update(INSERT_QUERY, params, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
        } else {
            throw new InsertException("Не удалось добавить данные пользователя в БД");
        }
        return user;
    }

    @Override
    public User update(User updUser) {
        removeAllUsersFriendships(updUser.getId());
        HashMap<String, Object> params = new HashMap<>();
        params.put("email", updUser.getEmail());
        params.put("login", updUser.getLogin());
        params.put("name", updUser.getName());
        params.put("birthday", java.sql.Date.valueOf(updUser.getBirthday()));
        params.put("id", updUser.getId());
        if (jdbc.update(UPDATE_USER_BY_ID, params) == 0) {
            throw new InsertException("Пользователь не найден в БД");
        }
        addAllUsersFriendships(updUser);
        return updUser;
    }

    @Override
    public void setUserAsFriend(Long initiatorUserId, Long targetUserId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user1_id", initiatorUserId);
        params.put("user2_id", targetUserId);
        jdbc.update(FRIENDSHIP_REQUEST, params);
    }

    @Override
    public void removeFromFriends(Long initiatorUserId, Long targetUserId) {
        if (!isUserExist(initiatorUserId)) {
            throw new NotFoundException("Пользователя с id = " + initiatorUserId + " не существует");
        }
        if (!isUserExist(targetUserId)) {
            throw new NotFoundException("Пользователя с id = " + targetUserId + " не существует");
        }
        if (!isUserAFriend(initiatorUserId, targetUserId) && isUserExist(targetUserId)) return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("user1_id", initiatorUserId);
        params.put("user2_id", targetUserId);
        jdbc.update(END_FRIENDSHIP, params);
    }

    @Override
    public Set<Long> findAllFriendsIds(Long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user1_id", userId);
        return jdbc.query(SELECT_ALL_FRIENDS_IDS_BY_ID, params, (ResultSet rs) -> {
            Set<Long> allFriends = new HashSet<>();
            while (rs.next()) {
                allFriends.add(rs.getLong("user2_id"));
            }
            return allFriends;
        });
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("friend_id", friendId);
        List<User> allUsers = jdbc.query(COMMON_FRIENDS, params, userRowMapper);
        return allUsers.stream()
                .peek(user -> user.setFriendsIds(findAllFriendsIds(user.getId())))
                .toList();
    }

    @Override
    public void delete(Long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", userId);
        jdbc.update(DELETE_QUERY, params);
    }


    private void addAllUsersFriendships(User user) {
        if (user.getFriendsIds() == null) return;
        SqlParameterSource[] mapSqlParameterSources = user.getFriendsIds().stream()
                .map(fId -> new MapSqlParameterSource()
                        .addValue("user1_id", user.getId())
                        .addValue("user2_id", fId))
                .toArray(MapSqlParameterSource[]::new);
        jdbc.batchUpdate(FRIENDSHIP_REQUEST, mapSqlParameterSources);
    }

    private void removeAllUsersFriendships(Long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user1_id", userId);
        jdbc.update(DELETE_FRIENDSHIPS, params);
    }
}
