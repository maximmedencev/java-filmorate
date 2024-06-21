package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;


    public Film find(Long filmId) {
        Film film = filmStorage.find(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм  с id = " + filmId + " не найден");
        }
        return film;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film updFilm) {
        Film film = filmStorage.find(updFilm.getId());
        if (film == null) {
            throw new NotFoundException("Фильм  с id = " + updFilm.getId() + " не найден");
        }
        return filmStorage.update(updFilm);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void like(Long filmId, Long userId) {
        Film film = filmStorage.find(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм  с id = " + filmId + " не найден");
        }
        User user = userService.find(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь  с id = " + userId + " не найден");
        }
        if (film.getLikedUsersIds() == null) {
            film.setLikedUsersIds(new HashSet<>());
        }
        film.getLikedUsersIds().add(userId);
    }

    public void unlike(Long filmId, Long userId) {
        Film film = filmStorage.find(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм  с id = " + filmId + " не найден");
        }
        User user = userService.find(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь  с id = " + userId + " не найден");
        }
        film.getLikedUsersIds().remove(userId);
    }

    public List<Film> getTopFilms(Long count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(film -> film.getLikedUsersIds().size(), Comparator.reverseOrder()))
                .limit(count)
                .toList();
    }

}
