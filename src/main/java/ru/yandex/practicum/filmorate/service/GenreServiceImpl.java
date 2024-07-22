package ru.yandex.practicum.filmorate.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genresStorage;

    public GenreServiceImpl(GenreRepository genresStorage) {
        this.genresStorage = genresStorage;
    }

    @Override
    public Collection<Genre> findAll() {
        return genresStorage.findAll();
    }

    @Override
    public Optional<Genre> find(Long genreId) {
        try {
            return genresStorage.find(genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр не найден");
        }
    }
}
