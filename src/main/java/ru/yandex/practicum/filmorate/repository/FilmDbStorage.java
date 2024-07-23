package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InsertException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
public class FilmDbStorage implements FilmRepository {
    private static final String SELECT_ALL_FILMS = "SELECT * FROM films";
    private static final String FIND_FILM_BY_ID = "SELECT * FROM films WHERE id = :id";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id) " +
            "VALUES (:name, :description, :release_date, :duration, :mpa_id)";
    private static final String UPDATE_FILM_BY_ID = "UPDATE films " +
            "SET name = :name, description = :description, release_date = :release_date," +
            " duration = :duration, mpa_id = :mpa_id WHERE Id = :id";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = :id";
    private static final String LIKE = "INSERT INTO likes(film_id, user_id) VALUES (:film_id, :user_id)";
    private static final String UNLIKE = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id";
    private static final String DELETE_LIKES_BY_FILM_ID = "DELETE FROM likes WHERE film_id = :film_id";
    private static final String LIKED_USERS_BY_FILM_ID = "SELECT user_id FROM likes WHERE film_id = :film_id";
    private static final String TOP_FILMS = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, " +
            "COUNT(l.film_id) AS likes_count " +
            "FROM films AS f " +
            "INNER JOIN likes AS l ON f.id = l.film_id " +
            "GROUP BY f.id " +
            "ORDER BY likes_count DESC " +
            "LIMIT :limit";
    private static final String ADD_FILM_GENRE = "INSERT INTO genres_film(film_id, genre_id) VALUES(:film_id, :genre_id)";
    private static final String REMOVE_FILM_GENRE = "DELETE FROM genres_film WHERE film_id = :film_id AND genre_id = :genre_id";
    private static final String DELETE_GENRES_BY_FILM_ID = "DELETE FROM genres_film WHERE film_id = :film_id";
    private static final String FILM_GENRES = "SELECT * FROM genres_film WHERE film_id = :film_id";

    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper filmRowMapper;

    @Autowired
    public FilmDbStorage(NamedParameterJdbcOperations jdbc,
                         FilmRowMapper filmRowMapper) {
        this.jdbc = jdbc;
        this.filmRowMapper = filmRowMapper;
    }

    @Override
    public Collection<Film> findAll() {
        return jdbc.query(SELECT_ALL_FILMS, filmRowMapper).stream()
                .peek(film -> film.setLikedUsersIds(getLikedUsersIds(film.getId())))
                .toList();
    }

    @Override
    public Optional<Film> find(long filmId) {
        Film film = null;
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", filmId);
            film = jdbc.queryForObject(FIND_FILM_BY_ID, params, filmRowMapper);
            if (film != null) {
                film.setLikedUsersIds(getLikedUsersIds(filmId));
            }
        } catch (EmptyResultDataAccessException ignored) {
            log.error("Получен пустой набор записей при попытке поиска по filmId = {}",
                    filmId);
        }
        return Optional.ofNullable(film);
    }

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Long mpaId = null;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", java.sql.Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", mpaId);
        jdbc.update(INSERT_QUERY, params, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
        } else {
            throw new InsertException("Не удалось добавить фильм в БД");
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addGenresToFilm(film, film.getGenres());
        }
        return film;
    }

    @Override
    public Film update(Film updFilm) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", updFilm.getName());
        params.put("description", updFilm.getDescription());
        params.put("release_date", java.sql.Date.valueOf(updFilm.getReleaseDate()));
        params.put("duration", updFilm.getDuration());
        params.put("mpa_id", updFilm.getMpa().getId());
        params.put("id", updFilm.getId());
        if (updFilm.getMpa() != null) {
            params.put("mpa_id", updFilm.getMpa().getId());
            updFilm.setMpa(Mpa.builder().id(updFilm.getMpa().getId()).build());
            deleteLikesByFilmId(updFilm.getId());
            addLikes(updFilm);
        } else {
            params.put("mpa_id", null);
        }
        if (jdbc.update(UPDATE_FILM_BY_ID, params) == 0) {
            throw new InsertException("Фильм не найден в БД");
        }
        removeGenresByFilmId(updFilm.getId());
        addGenresToFilm(updFilm, updFilm.getGenres());
        return updFilm;
    }

    @Override
    public void like(Long filmId, Long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        params.put("user_id", userId);
        jdbc.update(LIKE, params);
    }

    @Override
    public void unlike(Long filmId, Long userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        params.put("user_id", userId);
        jdbc.update(UNLIKE, params);
    }

    private Set<Long> getLikedUsersIds(Long filmId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        return jdbc.query(LIKED_USERS_BY_FILM_ID, params, (ResultSet rs) -> {
            Set<Long> usersIds = new HashSet<>();
            while (rs.next()) {
                usersIds.add(rs.getLong("user_id"));
            }
            return usersIds;
        });
    }

    @Override
    public void delete(Long filmId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        jdbc.update(DELETE_QUERY, params);
    }

    private void deleteLikesByFilmId(Long filmId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        jdbc.update(DELETE_LIKES_BY_FILM_ID, params);
    }

    private void addLikes(Film film) {
        if (film.getLikedUsersIds() == null) return;
        SqlParameterSource[] mapSqlParameterSources = film.getLikedUsersIds().stream()
                .map(likedUserId -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("user_id", likedUserId))
                .toArray(MapSqlParameterSource[]::new);
        jdbc.batchUpdate(LIKE, mapSqlParameterSources);
    }

    @Override
    public List<Film> getTopFilms(Long limit) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        return jdbc.query(TOP_FILMS, params, filmRowMapper);
    }

    private void addGenresToFilm(Film film, Set<Genre> genres) {
        if (genres == null) return;
        SqlParameterSource[] mapSqlParameterSources = genres.stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("genre_id", genre.getId()))
                .toArray(MapSqlParameterSource[]::new);
        jdbc.batchUpdate(ADD_FILM_GENRE, mapSqlParameterSources);
    }

    private void removeGenreFromFilm(Long filmId, Long genreId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        params.put("genre_id", genreId);
        jdbc.update(REMOVE_FILM_GENRE, params);
    }

    private void removeGenresByFilmId(Long filmId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        jdbc.update(DELETE_GENRES_BY_FILM_ID, params);
    }

    @Override
    public Set<Long> getFilmGenres(Long filmId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("film_id", filmId);
        return jdbc.query(FILM_GENRES, params, (ResultSet rs) -> {
            Set<Long> genresIds = new HashSet<>();
            while (rs.next()) {
                genresIds.add(rs.getLong("genre_id"));
            }
            return genresIds;
        });
    }
}

