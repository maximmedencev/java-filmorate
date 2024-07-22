package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@Import({GenreDbStorage.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("GenreRepository")
class GenreDbStorageTest {
    public static final long TEST_GENRE_ID = 1L;
    public static final long CREATED_TEST_GENRE_ID = 7L;

    private final GenreRepository genreStorage;

    static Genre getTestGenre() {
        return Genre.builder()
                .id(TEST_GENRE_ID)
                .name("Комедия")
                .build();
    }

    @Test
    @DisplayName("Должен возвращать жанр по id")
    public void shouldReturnGenreById() {
        Genre genre = genreStorage.find(TEST_GENRE_ID).get();
        assertThat(genre)
                .usingRecursiveComparison()
                .isEqualTo(getTestGenre());
    }

    static Collection<Genre> getAllTestGenres() {
        Collection<Genre> allGenres = new ArrayList<>();
        allGenres.add(Genre.builder()
                .id(1L)
                .name("Комедия")
                .build());
        allGenres.add(Genre.builder()
                .id(2L)
                .name("Драма")
                .build());
        allGenres.add(Genre.builder()
                .id(3L)
                .name("Мультфильм")
                .build());
        allGenres.add(Genre.builder()
                .id(4L)
                .name("Триллер")
                .build());
        allGenres.add(Genre.builder()
                .id(5L)
                .name("Документальный")
                .build());
        allGenres.add(Genre.builder()
                .id(6L)
                .name("Боевик")
                .build());
        return allGenres;
    }

    @Test
    @DisplayName("Должен возвращать все жанры")
    public void shouldReturnAllGenres() {
        Collection<Genre> genre = genreStorage.findAll();
        assertThat(genre)
                .usingRecursiveComparison()
                .isEqualTo(getAllTestGenres());
    }


    static Genre getGenreToCreate() {
        return Genre.builder()
                .id(CREATED_TEST_GENRE_ID)
                .name("createdGenreName")
                .build();
    }

    @Test
    @DisplayName("Должен создавать жанр")
    public void shouldCreateGenre() {
        Genre genre = Genre.builder()
                .name("createdGenreName")
                .build();
        assertThat(genreStorage.create(genre))
                .usingRecursiveComparison()
                .isEqualTo(getGenreToCreate());
        Optional<Genre> genreOptional = genreStorage.find(CREATED_TEST_GENRE_ID);
        assertThat(genreOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getGenreToCreate());
    }

    static Genre getGenreToUpdate() {
        return Genre.builder()
                .id(TEST_GENRE_ID)
                .name("updateGenreName")
                .build();
    }

    @Test
    @DisplayName("Должен обновлять жанр")
    public void shouldUpdateGenre() {
        Genre genre = Genre.builder()
                .id(TEST_GENRE_ID)
                .name("updateGenreName")
                .build();
        assertThat(genreStorage.update(genre))
                .usingRecursiveComparison()
                .isEqualTo(getGenreToUpdate());
        Optional<Genre> genreOptional = genreStorage.find(TEST_GENRE_ID);
        assertThat(genreOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getGenreToUpdate());
    }

    @Test
    @DisplayName("Должен удалять жанр с указанным id")
    public void shouldRemoveGenreWhenSpecifiedId() {
        genreStorage.delete(TEST_GENRE_ID);
        assertThat(genreStorage.find(TEST_GENRE_ID)).isEqualTo(Optional.empty());
    }
}