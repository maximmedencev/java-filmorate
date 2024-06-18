package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmStorage filmStorage = new InMemoryFilmStorage();

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updFilm) {
        return filmStorage.update(updFilm);
    }

}
