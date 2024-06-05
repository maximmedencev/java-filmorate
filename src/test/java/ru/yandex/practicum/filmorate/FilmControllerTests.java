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
        Film film = new Film();
        film.setName("");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDescription("Film description");
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionLengthOver200SymbolsWhenCreate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDescription("a".repeat(201));
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionForEarlyDateWhenCreate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(1500, 12, 12));
        film.setDescription("film description");
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenDurationLessThanZeroWhenCreate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(-30);
        film.setReleaseDate(LocalDate.of(2020, 12, 12));
        film.setDescription("film description");
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsEmptyWhenUpdate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDescription("Film description");

        filmController.create(film);

        Film filmUpdated = new Film();
        filmUpdated.setId(film.getId());
        filmUpdated.setName("");
        filmUpdated.setDuration(30);
        filmUpdated.setReleaseDate(LocalDate.of(2000, 12, 12));
        filmUpdated.setDescription("Film description");

        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }

    @Test
    void shouldThrowValidationExceptionWhenDescriptionLengthOver200SymbolsWhenUpdate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDescription("Film description");

        filmController.create(film);

        Film filmUpdated = new Film();
        filmUpdated.setId(film.getId());
        filmUpdated.setName("Film name");
        filmUpdated.setDuration(30);
        filmUpdated.setReleaseDate(LocalDate.of(2000, 12, 12));
        filmUpdated.setDescription("a".repeat(201));
        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }

    @Test
    void shouldThrowValidationExceptionForEarlyDateWhenUpdate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDescription("Film description");

        filmController.create(film);

        Film filmUpdated = new Film();
        filmUpdated.setId(film.getId());
        filmUpdated.setName("Film name");
        filmUpdated.setDuration(30);
        filmUpdated.setReleaseDate(LocalDate.of(1500, 12, 12));
        filmUpdated.setDescription("Film description");
        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }


    @Test
    void shouldThrowValidationExceptionWhenDurationLessThanZeroWhenUpdate() {
        Film film = new Film();
        film.setName("Film name");
        film.setDuration(30);
        film.setReleaseDate(LocalDate.of(2000, 12, 12));
        film.setDescription("Film description");

        filmController.create(film);

        Film filmUpdated = new Film();
        filmUpdated.setId(film.getId());
        filmUpdated.setName("Film name");
        filmUpdated.setDuration(-30);
        filmUpdated.setReleaseDate(LocalDate.of(2020, 12, 12));
        filmUpdated.setDescription("Film description");
        Assertions.assertThrows(ValidationException.class, () -> filmController.update(filmUpdated));
    }

}
