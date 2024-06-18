package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film updFilm) {
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
}
