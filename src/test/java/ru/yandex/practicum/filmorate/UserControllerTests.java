package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTests {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsEmptyWhenCreate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsWrongWhenCreate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("emailwithoutatsymbol")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsEmptyWhenCreate() {
        User user = User.builder()
                .name("Bill")
                .login("")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainsSpacesWhenCreate() {
        User user = User.builder()
                .name("Bill")
                .login("Login with spaces")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void loginShouldEqualNameWhenNameIsEmptyWhenCreate() {
        User user = User.builder()
                .name("")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthDayIsInFutureWhenCreate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.now().plusDays(1))
                .email("email@company.com")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsEmptyWhenUpdate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);

        User userUpdated = User.builder()
                .id(user.getId())
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsWrongWhenUpdate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);

        User userUpdated = User.builder()
                .id(user.getId())
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("wrongemail")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsEmptyWhenUpdate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);

        User userUpdated = User.builder()
                .id(user.getId())
                .name("Bill")
                .login("")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainsSpacesWhenUpdate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);

        User userUpdated = User.builder()
                .id(user.getId())
                .name("Bill")
                .login("Login with spaces")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void loginShouldEqualNameWhenNameIsEmptyWhenUpdate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);

        User userUpdated = User.builder()
                .id(user.getId())
                .name("")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.update(userUpdated);

        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthDayIsInFutureWhenUpdate() {
        User user = User.builder()
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.of(1988, 12, 12))
                .email("email@company.com")
                .build();
        userController.create(user);

        User userUpdated = User.builder()
                .id(user.getId())
                .name("Bill")
                .login("Lucky")
                .birthday(LocalDate.now().plusDays(1))
                .email("email@company.com")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }
}
