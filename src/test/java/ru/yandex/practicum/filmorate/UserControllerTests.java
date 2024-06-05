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
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsWrongWhenCreate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("emailwithoutatsymbol");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsEmptyWhenCreate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainsSpacesWhenCreate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Login with spaces");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void loginShouldEqualNameWhenNameIsEmptyWhenCreate() {
        User user = new User();
        user.setName("");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");
        userController.create(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthDayIsInFutureWhenCreate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.now().plusDays(1));
        user.setEmail("email@company.com");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsEmptyWhenUpdate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");

        User userUpdated = new User();
        userUpdated.setId(user.getId());
        userUpdated.setName("Bill");
        userUpdated.setLogin("Lucky");
        userUpdated.setBirthday(LocalDate.of(1988, 12, 12));
        userUpdated.setEmail("");

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsWrongWhenUpdate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");

        User userUpdated = new User();
        userUpdated.setId(user.getId());
        userUpdated.setName("Bill");
        userUpdated.setLogin("Lucky");
        userUpdated.setBirthday(LocalDate.of(1988, 12, 12));
        userUpdated.setEmail("wrongemail");

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginIsEmptyWhenUpdate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");

        User userUpdated = new User();
        userUpdated.setId(user.getId());
        userUpdated.setName("Bill");
        userUpdated.setLogin("");
        userUpdated.setBirthday(LocalDate.of(1988, 12, 12));
        userUpdated.setEmail("wrongemail");

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainsSpacesWhenUpdate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");

        User userUpdated = new User();
        userUpdated.setId(user.getId());
        userUpdated.setName("Bill");
        userUpdated.setLogin("Login with spaces");
        userUpdated.setBirthday(LocalDate.of(1988, 12, 12));
        userUpdated.setEmail("email@company.com");

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }

    @Test
    void loginShouldEqualNameWhenNameIsEmptyWhenUpdate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");
        userController.create(user);

        User userUpdated = new User();
        userUpdated.setId(user.getId());
        userUpdated.setName("");
        userUpdated.setLogin("Lucky");
        userUpdated.setBirthday(LocalDate.of(1988, 12, 12));
        userUpdated.setEmail("email@company.com");
        userController.update(userUpdated);

        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthDayIsInFutureWhenUpdate() {
        User user = new User();
        user.setName("Bill");
        user.setLogin("Lucky");
        user.setBirthday(LocalDate.of(1988, 12, 12));
        user.setEmail("email@company.com");
        userController.create(user);

        User userUpdated = new User();
        userUpdated.setId(user.getId());
        userUpdated.setName("Bill");
        userUpdated.setLogin("Lucky");
        userUpdated.setBirthday(LocalDate.now().plusDays(1));
        userUpdated.setEmail("email@company.com");

        Assertions.assertThrows(ValidationException.class, () -> userController.update(userUpdated));
    }
}
