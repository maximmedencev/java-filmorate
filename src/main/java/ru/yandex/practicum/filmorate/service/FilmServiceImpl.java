package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final GenreRepository genresStorage;
    private final MpaRepository mpaStorage;

    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           GenreRepository genresStorage,
                           MpaRepository mpaStorage,
                           UserRepository userStorage) {
        this.filmStorage = filmStorage;
        this.genresStorage = genresStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Optional<Film> find(Long filmId) {
        Optional<Film> optFilm = filmStorage.find(filmId);
        Film film = null;
        if (optFilm.isPresent()) {
            film = optFilm.get();
            long mpaId = film.getMpa().getId();
            Optional<Mpa> optMpa = mpaStorage.find(film.getMpa().getId());
            if (optMpa.isPresent()) {
                film.setMpa(Mpa.builder().id(mpaId).name(optMpa.get().getName()).build());
            }
            film.setGenres(filmStorage.getFilmGenres(filmId).stream()
                    .map(genresStorage::find)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));
        }
        return Optional.ofNullable(film);
    }

    @Override
    public Film create(Film film) {
        try {
            filmStorage.create(film);
            if (film.getMpa() != null) {
                Optional<Mpa> optMpa = mpaStorage.find(film.getMpa().getId());
                optMpa.ifPresent(mpa -> film.setMpa(Mpa.builder()
                        .id(film.getMpa().getId())
                        .name(mpa.getName())
                        .build()));
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Неправильно составлен запрос");
        }
        return film;
    }

    @Override
    public Film update(Film updFilm) {
        if (updFilm.getMpa() != null && !mpaStorage.isMpaExist(updFilm.getMpa().getId())) {
            throw new BadRequestException("Рейтинга с id = " + updFilm.getMpa().getId() + " не существует");
        }
        filmStorage.update(updFilm);
        if (updFilm.getMpa() != null) {
            Optional<Mpa> optMpa = mpaStorage.find(updFilm.getMpa().getId());
            optMpa.ifPresent(mpa -> updFilm.setMpa(Mpa.builder()
                    .id(updFilm.getMpa().getId())
                    .name(mpa.getName())
                    .build()));
        }
        return updFilm;
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll().stream()
                .peek(film -> film.setMpa(mpaStorage.find(film.getMpa().getId()).get()))
                .toList();
    }

    @Override
    public void like(Long filmId, Long userId) {
        try {
            filmStorage.like(filmId, userId);
        } catch (DuplicateKeyException ignored) {
            log.error("Попытка добавить запись(filmId = {},userId = {}) уже имеющуюся в таблице likes"
                    , filmId, userId);
        }
    }

    @Override
    public void unlike(Long filmId, Long userId) {
        filmStorage.unlike(filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Long limit) {
        return filmStorage.getTopFilms(limit);
    }
}
