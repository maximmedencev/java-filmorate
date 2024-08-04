package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Optional<User> find(long userId) {
        return userStorage.find(userId);
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User updUser) {
        userStorage.update(updUser);
        return updUser;
    }

    @Override
    public void makeFriend(Long initiatorUserId, Long targetUserId) {
        try {
            userStorage.setUserAsFriend(initiatorUserId, targetUserId);
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Указанный пользователь не найден");
        }
    }

    @Override
    public void unmakeFriend(Long initiatorUserId, Long targetUserId) {
        userStorage.removeFromFriends(initiatorUserId, targetUserId);
    }

    @Override
    public List<User> getCommonFriendsList(Long userId, Long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = userStorage.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        return user.getFriendsIds().stream()
                .map(this::find)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public void delete(Long userId) {
        userStorage.delete(userId);
    }
}
