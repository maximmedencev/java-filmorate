package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@Import({MpaDbStorage.class, MpaRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("MpaRepository")
class MpaDbStorageTest {
    public static final long TEST_MPA_ID = 1L;
    public static final long NOT_EXISTING_MPA_ID = 999L;

    private final MpaRepository mpaStorage;

    static Mpa getTestMpa() {
        return Mpa.builder()
                .id(TEST_MPA_ID)
                .name("G")
                .build();
    }

    @Test
    @DisplayName("Должен возвращать MPA по id")
    public void shouldReturnMpaById() {
        Mpa mpa = mpaStorage.find(TEST_MPA_ID).get();
        assertThat(mpa)
                .usingRecursiveComparison()
                .isEqualTo(getTestMpa());
    }

    static Collection<Mpa> getAllTestMpa() {
        Collection<Mpa> allMpa = new ArrayList<>();
        allMpa.add(Mpa.builder()
                .id(1L)
                .name("G")
                .build());
        allMpa.add(Mpa.builder()
                .id(2L)
                .name("PG")
                .build());
        allMpa.add(Mpa.builder()
                .id(3L)
                .name("PG-13")
                .build());
        allMpa.add(Mpa.builder()
                .id(4L)
                .name("R")
                .build());
        allMpa.add(Mpa.builder()
                .id(5L)
                .name("NC-17")
                .build());
        return allMpa;
    }

    @Test
    @DisplayName("Должен возвращать все MPA")
    public void shouldReturnAllMpa() {
        Collection<Mpa> mpa = mpaStorage.findAll();
        assertThat(mpa)
                .usingRecursiveComparison()
                .isEqualTo(getAllTestMpa());
    }

    @Test
    @DisplayName("Должен возвращать истину, если MPA существует")
    public void shouldReturnTrueWhenMpaWithSpecifiedIdExist() {
        assertThat(mpaStorage.isMpaExist(TEST_MPA_ID))
                .isEqualTo(true);
        assertThat(mpaStorage.isMpaExist(NOT_EXISTING_MPA_ID))
                .isEqualTo(false);
    }
}