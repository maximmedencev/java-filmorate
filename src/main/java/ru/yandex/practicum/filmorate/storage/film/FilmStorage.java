package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Collection<Film> findAll();

    Optional<Film> find(long filmId);

    Film create(Film film);

    Film update(Film updFilm);

    void like(Long filmId, Long userId);

    void unlike(Long filmId, Long userId);

    void delete(Long filmId);

    List<Film> getTopFilms(Long limit);

    Set<Long> getFilmGenres(Long filmId);
}
