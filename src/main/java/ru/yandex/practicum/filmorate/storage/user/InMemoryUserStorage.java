package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User find(long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getFriendsIds() == null)
            user.setFriendsIds(HashSet.newHashSet(0));
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @Override
    public User update(User updUser) {
        if (users.containsKey(updUser.getId())) {
            User oldUser = users.get(updUser.getId());
            log.info("Идет изменение данных пользователя с {} на {}", oldUser, updUser);
            if (updUser.getName().isBlank()) {
                oldUser.setName(updUser.getLogin());
            } else {
                oldUser.setName(updUser.getName());
            }
            oldUser.setEmail(updUser.getEmail());
            oldUser.setLogin(updUser.getLogin());
            oldUser.setBirthday(updUser.getBirthday());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + updUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
