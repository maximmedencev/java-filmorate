package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User find(long userId) {
        User user = userStorage.find(userId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        return user;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User updUser) {
        if (userStorage.find(updUser.getId()) == null)
            throw new NotFoundException("Пользователь с id = " + updUser.getId() + " не найден");
        return userStorage.update(updUser);
    }

    public void makeFriends(Long userId, Long friendId) {
        User user = find(userId);
        User friend = find(friendId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (friend == null)
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
    }

    public void unmakeFriends(Long userId, Long friendId) {
        User user = find(userId);
        User friend = find(friendId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (friend == null)
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        user.getFriendsIds().remove(friendId);
        friend.getFriendsIds().remove(userId);
    }

    public List<User> getCommonFriendsList(Long userId, Long friendId) {
        User user = this.find(userId);
        User friend = this.find(friendId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (friend == null)
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");

        return user.getFriendsIds().stream()
                .filter(uid -> friend.getFriendsIds().contains(uid))
                .map(this::find)
                .toList();
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.find(userId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        return user.getFriendsIds().stream()
                .map(this::find)
                .toList();
    }

}
