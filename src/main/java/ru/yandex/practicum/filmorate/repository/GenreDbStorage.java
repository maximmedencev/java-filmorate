package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InsertException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class GenreDbStorage implements GenreRepository {
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genres";
    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE id = :id";
    private static final String INSERT_QUERY = "INSERT INTO genres(name) VALUES(:name)";
    private static final String UPDATE_QUERY = "UPDATE genres SET name = :name WHERE id = :id";
    private static final String DELETE_QUERY = "DELETE FROM genres WHERE id = :id";

    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public Collection<Genre> findAll() {
        return this.jdbc.query(SELECT_ALL_GENRES, genreRowMapper);
    }

    @Override
    public Optional<Genre> find(Long genreId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", genreId);
        Genre genre = null;
        try {
            genre = this.jdbc.queryForObject(SELECT_GENRE_BY_ID, params, genreRowMapper);
        } catch (EmptyResultDataAccessException ignored) {
        }
        return Optional.ofNullable(genre);
    }

    @Override
    public Genre create(Genre genre) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", genre.getName());
        jdbc.update(INSERT_QUERY, params, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            genre.setId(id);
        } else {
            throw new InsertException("Не удалось добавить жанр в БД");
        }
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", genre.getId())
                .addValue("name", genre.getName());
        jdbc.update(UPDATE_QUERY, params);
        return genre;
    }

    @Override
    public void delete(Long genreId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", genreId);
        jdbc.update(DELETE_QUERY, params);
    }
}

