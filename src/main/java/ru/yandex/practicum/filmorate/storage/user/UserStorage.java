package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface UserStorage {
    User find(long userId);

    Collection<User> findAll();

    User create(User user);

    User update(User updUser);

}
