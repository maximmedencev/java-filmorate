package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import({UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("UserRepository")
class UserDbStorageTest {
    private final UserRepository userStorage;

    public static final long TEST_USER_ID = 1L;
    public static final long CREATED_TEST_USER_ID = 6L;
    public static final long FRIENDSHIP_USER_1 = 4L;
    public static final long FRIENDSHIP_USER_2 = 5L;
    public static final long REMOVE_FRIENDSHIP_USER_1 = 1L;
    public static final long REMOVE_FRIENDSHIP_USER_2 = 2L;
    public static final List<Long> USER_1_FRIENDS_AFTER_REMOVE = List.of(3L, 5L);
    public static final long FIRST_FRIEND_ID = 1L;
    public static final long SECOND_FRIEND_ID = 2L;
    public static final long FIRST_COMMON_FRIEND_ID = 3L;
    public static final long SECOND_COMMON_FRIEND_ID = 5L;

    static User getTestUser() {
        return User.builder()
                .id(TEST_USER_ID)
                .email("user1@company.com")
                .name("name1")
                .login("login1")
                .birthday(LocalDate.of(1991, 12, 12))
                .friendsIds(new HashSet<>(List.of(2L, 3L, 5L)))
                .build();
    }

    @Test
    @DisplayName("Должен находить пользователя по id")
    public void shouldReturnUserWhenFindById() {
        Optional<User> userOptional = userStorage.find(TEST_USER_ID);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestUser());
    }

    static Collection<User> getTestUsers() {
        Collection<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .email("user1@company.com")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1991, 12, 12))
                .friendsIds(new HashSet<>(List.of(2L, 3L, 5L)))
                .build());
        users.add(User.builder()
                .id(2)
                .email("user2@company.com")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(1992, 12, 12))
                .friendsIds(new HashSet<>(List.of(3L, 4L, 5L)))
                .build());
        for (int i = 3; i <= 5; i++) {
            users.add(User.builder()
                    .id(i)
                    .email("user" + i + "@company.com")
                    .login("login" + i)
                    .name("name" + i)
                    .birthday(LocalDate.of(1990 + i, 12, 12))
                    .friendsIds(new HashSet<>())
                    .build());
        }
        return users;
    }

    @Test
    @DisplayName("Должен находить всех пользователей")
    public void shouldReturnAllUsers() {
        Collection<User> users = userStorage.findAll();
        assertThat(users)
                .usingRecursiveComparison()
                .isEqualTo(getTestUsers());
    }

    static User getUserToCreate() {
        return User.builder()
                .id(CREATED_TEST_USER_ID)
                .email("createdUser@company.com")
                .login("createdUserLogin")
                .name("createdUserName")
                .birthday(LocalDate.of(1999, 9, 9))
                .friendsIds(new HashSet<>())
                .build();
    }

    @Test
    @DisplayName("Должен создавать пользователя")
    public void shouldCreateUser() {
        User user = User.builder()
                .email("createdUser@company.com")
                .login("createdUserLogin")
                .name("createdUserName")
                .birthday(LocalDate.of(1999, 9, 9))
                .friendsIds(new HashSet<>())
                .build();
        assertThat(userStorage.create(user))
                .usingRecursiveComparison()
                .isEqualTo(getUserToCreate());
        Optional<User> userOptional = userStorage.find(CREATED_TEST_USER_ID);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getUserToCreate());
    }

    static User getUserToUpdate() {
        return User.builder()
                .id(TEST_USER_ID)
                .email("updatedUser@company.com")
                .login("updatedUserLogin")
                .name("updatedUserName")
                .birthday(LocalDate.of(1999, 9, 9))
                .friendsIds(new HashSet<>(2, 3))
                .build();
    }

    @Test
    @DisplayName("Должен обновлять пользователя")
    public void shouldUpdateUser() {
        User user = User.builder()
                .id(TEST_USER_ID)
                .email("updatedUser@company.com")
                .login("updatedUserLogin")
                .name("updatedUserName")
                .birthday(LocalDate.of(1999, 9, 9))
                .friendsIds(new HashSet<>(2, 3))
                .build();
        assertThat(userStorage.update(user))
                .usingRecursiveComparison()
                .isEqualTo(getUserToUpdate());
        Optional<User> userOptional = userStorage.find(TEST_USER_ID);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getUserToUpdate());
    }

    @Test
    @DisplayName("Должен добавлять друзей по их id")
    public void shouldSetUserAsFriendWhenSpecifiedIds() {
        userStorage.setUserAsFriend(FRIENDSHIP_USER_1, FRIENDSHIP_USER_2);
        Optional<User> userOptional = userStorage.find(FRIENDSHIP_USER_1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("friendsIds",
                                        new HashSet<>(List.of(FRIENDSHIP_USER_2)))
                );
    }

    @Test
    @DisplayName("Должен удалять друзей по id")
    public void testRemoveFromFriend() {
        userStorage.removeFromFriends(REMOVE_FRIENDSHIP_USER_1, REMOVE_FRIENDSHIP_USER_2);
        Optional<User> userOptional = userStorage.find(REMOVE_FRIENDSHIP_USER_1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("friendsIds",
                                new HashSet<>(USER_1_FRIENDS_AFTER_REMOVE))
                );
    }

    static Collection<User> getCommonFriends() {
        Collection<User> commonFriends = new ArrayList<>();
        commonFriends.add(User.builder()
                .id(FIRST_COMMON_FRIEND_ID)
                .email("user3@company.com")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(1993, 12, 12))
                .friendsIds(new HashSet<>())
                .build());
        commonFriends.add(User.builder()
                .id(SECOND_COMMON_FRIEND_ID)
                .email("user5@company.com")
                .login("login5")
                .name("name5")
                .birthday(LocalDate.of(1995, 12, 12))
                .friendsIds(new HashSet<>())
                .build());
        return commonFriends;
    }

    @Test
    @DisplayName("Должен возвращать общих друзей")
    public void shouldReturnCommonFriendsIds() {
        List<User> commonFriends = userStorage.getCommonFriends(FIRST_FRIEND_ID, SECOND_FRIEND_ID);
        assertThat(userStorage.getCommonFriends(FIRST_FRIEND_ID, SECOND_FRIEND_ID))
                .usingRecursiveComparison()
                .isEqualTo(getCommonFriends());
    }

    @Test
    @DisplayName("Должен удалять пользователя по id")
    public void shouldDeleteUserWhenSpecifiedId() {
        userStorage.delete(TEST_USER_ID);
        assertThat(userStorage.find(TEST_USER_ID)).isEqualTo(Optional.empty());
    }
}