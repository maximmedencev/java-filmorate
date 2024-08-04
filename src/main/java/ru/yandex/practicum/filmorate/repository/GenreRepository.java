package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {
    Collection<Genre> findAll();

    Optional<Genre> find(Long genreId);

    Genre create(Genre genre);

    Genre update(Genre genre);

    void delete(Long genreId);
}
