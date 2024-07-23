package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    Optional<User> find(long userId);

    Collection<User> findAll();

    User create(User user);

    User update(User updUser);

    void setUserAsFriend(Long initiatorUserId, Long targetUserId);

    void removeFromFriends(Long initiatorUserId, Long targetUserId);

    Set<Long> findAllFriendsIds(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);

    void delete(Long userId);

    Boolean isUserAFriend(long user1Id, long user2Id);
}
