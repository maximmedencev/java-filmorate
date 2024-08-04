package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MpaDbStorage implements MpaRepository {
    private static final String SELECT_ALL_MPAS = "SELECT * FROM mpa";
    private static final String SELECT_MPA_BY_ID = "SELECT * FROM mpa WHERE id = :id";
    private static final String IS_MPA_EXIST_QUERY = "SELECT EXISTS(SELECT FROM mpa WHERE id = :id)";

    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Boolean isMpaExist(long mpaId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", mpaId);
        return jdbc.queryForObject(IS_MPA_EXIST_QUERY, params, Boolean.class);
    }

    @Override
    public Collection<Mpa> findAll() {
        return jdbc.query(SELECT_ALL_MPAS, mpaRowMapper);
    }

    @Override
    public Optional<Mpa> find(Long mpaId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", mpaId);
        Mpa mpa = null;
        try {
            mpa = jdbc.queryForObject(SELECT_MPA_BY_ID, params, mpaRowMapper);
        } catch (
                EmptyResultDataAccessException ignored) {
            log.error("Получен пустой набор записей при попытке поиска по mpaId = {}",
                    mpaId);
        }
        return Optional.ofNullable(mpa);
    }
}
