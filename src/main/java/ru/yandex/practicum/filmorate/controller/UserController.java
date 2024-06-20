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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

    @GetMapping("/{userId}")
    public User find(@PathVariable Long userId) {
        return userService.find(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void makeFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.makeFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void unmakeFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.unmakeFriends(userId, friendId);
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
