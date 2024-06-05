package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userValidation(user);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User updUser) {
        userValidation(updUser);
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

    private static void userValidation(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Получено недопустимое значение email {}", user.getEmail());
            throw new ValidationException("Некорректное значение email");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Получен недопустимый логин {}", user.getLogin());
            throw new ValidationException("Логин не должен быть пустым или содеражать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Получен недопустимая дата рождения {}", user.getBirthday());
            throw new ValidationException("Некорректная дата рождения");
        }

    }
}
