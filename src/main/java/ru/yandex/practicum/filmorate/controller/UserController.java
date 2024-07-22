package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsIds(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

    @GetMapping("/{userId}")
    public User find(@PathVariable Long userId) {
        return userService.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + userId + " не существует"));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void makeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.makeFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void unmakeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.unmakeFriend(userId, friendId);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User updUser) {
        return userService.update(updUser);
    }

}
