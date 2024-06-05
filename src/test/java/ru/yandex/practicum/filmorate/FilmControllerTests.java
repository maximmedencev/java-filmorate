package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTests {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsEmptyWhenCreate() {
        Film film = Film.builder()
                .name("")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionLengthOver200SymbolsWhenCreate() {
        Film film = Film.builder()
                .name("")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("a".repeat(201))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionForEarlyDateWhenCreate() {
        Film film = Film.builder()
                .name("Film")
                .duration(30)
                .releaseDate(LocalDate.of(1234, 12, 12))
                .description("Film description")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationLessThanZeroWhenCreate() {
        Film film = Film.builder()
                .name("Film")
                .duration(-30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsEmptyWhenUpdate() {
        Film film = Film.builder()
                .name("Film")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();
        filmController.create(film);

        Film filmUpdated = Film.builder()
                .name("")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionLengthOver200SymbolsWhenUpdate() {
        Film film = Film.builder()
                .name("Film")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();
        filmController.create(film);

        Film filmUpdated = Film.builder()
                .name("Film")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("a".repeat(201))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }

    @Test
    void shouldThrowValidationExceptionForEarlyDateWhenUpdate() {
        Film film = Film.builder()
                .name("Film")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();
        filmController.create(film);

        Film filmUpdated = Film.builder()
                .name("")
                .duration(30)
                .releaseDate(LocalDate.of(1000, 12, 12))
                .description("Film description")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }


    @Test
    void shouldThrowValidationExceptionWhenDurationLessThanZeroWhenUpdate() {
        Film film = Film.builder()
                .name("Film")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();
        filmController.create(film);

        Film filmUpdated = Film.builder()
                .name("Film")
                .duration(-30)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .description("Film description")
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }
}
