package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updFilm) {
        if (films.containsKey(updFilm.getId())) {
            Film oldFilm = films.get(updFilm.getId());
            log.info("Идет измененение данных фильма с {} на {}", oldFilm, updFilm);
            oldFilm.setName(updFilm.getName());
            oldFilm.setDescription(updFilm.getDescription());
            oldFilm.setDuration(updFilm.getDuration());
            oldFilm.setReleaseDate(updFilm.getReleaseDate());

            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + updFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private static void filmValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое название фильма");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.error("Описание фильма содержит {} символов. Максимум - 200",
                    film.getDescription().length());
            throw new ValidationException("Описание фильма длинее 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Получена дата релиза {}. Дата не должна быть раньше 1985-12-28",
                    film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.error("Получена продолжительность фильма {}. Продолжительность должна быть больше 0",
                    film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
