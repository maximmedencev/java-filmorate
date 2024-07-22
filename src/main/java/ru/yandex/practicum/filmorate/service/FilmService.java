package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmService {
    Optional<Film> find(Long filmId);

    Film create(Film film);

    Film update(Film updFilm);

    Collection<Film> findAll();

    void like(Long filmId, Long userId);

    void unlike(Long filmId, Long userId);

    List<Film> getTopFilms(Long count);

}
