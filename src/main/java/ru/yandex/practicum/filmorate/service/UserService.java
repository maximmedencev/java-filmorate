package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> find(long userId);

    Collection<User> findAll();

    User create(User user);

    User update(User updUser);

    void makeFriend(Long initiatorUserId, Long targetUserId);

    void unmakeFriend(Long initiatorUserId, Long targetUserId);

    List<User> getCommonFriendsList(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    void delete(Long userId);
}
