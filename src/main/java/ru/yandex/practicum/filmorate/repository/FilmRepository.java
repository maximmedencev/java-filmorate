package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmRepository extends FilmStorage {
    @Override
    Collection<Film> findAll();

    @Override
    Optional<Film> find(long filmId);

    @Override
    Film create(Film film);

    @Override
    Film update(Film updFilm);

    @Override
    void like(Long filmId, Long userId);

    @Override
    void unlike(Long filmId, Long userId);

    @Override
    void delete(Long filmId);

    @Override
    List<Film> getTopFilms(Long limit);

    @Override
    Set<Long> getFilmGenres(Long filmId);
}

