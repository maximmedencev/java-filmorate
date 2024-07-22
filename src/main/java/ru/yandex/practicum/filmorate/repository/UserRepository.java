package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends UserStorage {

    @Override
    Optional<User> find(long userId);

    @Override
    Collection<User> findAll();

    @Override
    User create(User user);

    @Override
    User update(User updUser);

    @Override
    void setUserAsFriend(Long initiatorUserId, Long targetUserId);

    @Override
    void removeFromFriends(Long initiatorUserId, Long targetUserId);

    @Override
    Set<Long> findAllFriendsIds(Long userId);

    @Override
    List<User> getCommonFriends(Long userId, Long friendId);

    @Override
    void delete(Long userId);

}
