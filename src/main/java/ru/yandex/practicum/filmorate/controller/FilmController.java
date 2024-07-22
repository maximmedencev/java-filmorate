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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")

public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable Long id, @PathVariable Long userId) {
        filmServiceImpl.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlike(@PathVariable Long id, @PathVariable Long userId) {
        filmServiceImpl.unlike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10") Long count) {
        return filmServiceImpl.getTopFilms(count);
    }

    @GetMapping("/{filmId}")
    public Film find(@PathVariable long filmId) {
        return filmServiceImpl.find(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + filmId + " не существует"));
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmServiceImpl.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmServiceImpl.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updFilm) {
        return filmServiceImpl.update(updFilm);
    }

}
