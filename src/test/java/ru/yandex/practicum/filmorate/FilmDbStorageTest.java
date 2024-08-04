package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import({FilmDbStorage.class, FilmRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("FilmRepository")
class FilmDbStorageTest {
    private final FilmRepository filmStorage;
    public static final long TEST_FILM_ID = 1L;
    public static final long TEST_FILM_TO_LIKE_ID = 5L;
    public static final long CREATED_TEST_FILM_ID = 6L;
    public static final long TEST_USER_ID1 = 4L;
    public static final long TEST_USER_ID2 = 1L;

    static Film getTestFilm() {
        return Film.builder()
                .id(TEST_FILM_ID)
                .name("name1")
                .description("description1")
                .duration(100)
                .mpa(Mpa.builder().id(1L).name(null).build())
                .likedUsersIds(new HashSet<>(List.of(1L, 2L, 3L, 4L)))
                .releaseDate(LocalDate.of(2000, 10, 11))
                .build();
    }

    @Test
    @DisplayName("Должен находить фильм по id")
    public void shouldReturnFilmWhenFindById() {
        Optional<Film> filmOptional = filmStorage.find(TEST_FILM_ID);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm());
    }

    static Collection<Film> getTestFilms() {
        Collection<Film> films = new ArrayList<>();
        films.add(
                Film.builder()
                        .id(1)
                        .name("name1")
                        .description("description1")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 11))
                        .likedUsersIds(new HashSet<>(List.of(1L, 2L, 3L, 4L)))
                        .mpa(Mpa.builder().id(1L).build())
                        .build()
        );

        films.add(
                Film.builder()
                        .id(2)
                        .name("name2")
                        .description("description2")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 12))
                        .likedUsersIds(new HashSet<>(List.of(3L)))
                        .mpa(Mpa.builder().id(2L).build())
                        .build()
        );

        films.add(
                Film.builder()
                        .id(3)
                        .name("name3")
                        .description("description3")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 13))
                        .likedUsersIds(new HashSet<>(List.of(1L, 2L, 5L)))
                        .mpa(Mpa.builder().id(3L).build())
                        .build()
        );

        films.add(
                Film.builder()
                        .id(4)
                        .name("name4")
                        .description("description4")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 14))
                        .likedUsersIds(new HashSet<>(List.of(5L)))
                        .mpa(Mpa.builder().id(4L).build())
                        .build()
        );

        films.add(
                Film.builder()
                        .id(5)
                        .name("name5")
                        .description("description5")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 15))
                        .likedUsersIds(new HashSet<>(List.of(2L, 3L)))
                        .mpa(Mpa.builder().id(5L).build())
                        .build()
        );
        return films;
    }

    @Test
    @DisplayName("Должен находить все фильмы")
    public void shouldReturnAllFilms() {
        Collection<Film> films = filmStorage.findAll();
        assertThat(films)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilms());
    }

    static Film getFilmToCreate() {
        return Film.builder()
                .id(CREATED_TEST_FILM_ID)
                .name("createdFilmName")
                .description("createdFilmDescription")
                .duration(100)
                .releaseDate(LocalDate.of(2000, 10, 10))
                .likedUsersIds(new HashSet<>())
                .mpa(Mpa.builder().id(1L).build())
                .build();
    }

    @Test
    @DisplayName("Должен создавать фильм")
    public void shouldCreateUser() {
        Film film = Film.builder()
                .id(CREATED_TEST_FILM_ID)
                .name("createdFilmName")
                .description("createdFilmDescription")
                .duration(100)
                .releaseDate(LocalDate.of(2000, 10, 10))
                .likedUsersIds(new HashSet<>())
                .mpa(Mpa.builder().id(1L).build())
                .build();
        assertThat(filmStorage.create(film))
                .usingRecursiveComparison()
                .isEqualTo(getFilmToCreate());
        Optional<Film> filmOptional = filmStorage.find(CREATED_TEST_FILM_ID);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getFilmToCreate());
    }

    static Film getFilmToUpdate() {
        return Film.builder()
                .id(TEST_FILM_ID)
                .name("updatedFilmName")
                .description("updatedFilmDescription")
                .duration(101)
                .mpa(Mpa.builder().id(1L).build())
                .likedUsersIds(new HashSet<>(List.of(1L, 2L)))
                .releaseDate(LocalDate.of(2000, 10, 10))
                .build();
    }

    @Test
    @DisplayName("Должен обновлять фильм")
    public void shouldUpdateFilm() {
        Film film = Film.builder()
                .id(TEST_FILM_ID)
                .name("updatedFilmName")
                .description("updatedFilmDescription")
                .duration(101)
                .releaseDate(LocalDate.of(2000, 10, 10))
                .likedUsersIds(new HashSet<>(List.of(1L, 2L)))
                .mpa(Mpa.builder().id(1L).build())
                .build();
        assertThat(filmStorage.update(film))
                .usingRecursiveComparison()
                .isEqualTo(getFilmToUpdate());
        Optional<Film> filmOptional = filmStorage.find(TEST_FILM_ID);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getFilmToUpdate());
    }

    static Film getLikedFilm() {
        return Film.builder()
                .id(TEST_FILM_TO_LIKE_ID)
                .name("name5")
                .description("description5")
                .duration(100)
                .mpa(Mpa.builder().id(5L).name(null).build())
                .releaseDate(LocalDate.of(2000, 10, 15))
                .likedUsersIds(new HashSet<>(List.of(2L, 3L, TEST_USER_ID1)))
                .build();
    }

    @Test
    @DisplayName("Должен добавлять лайк")
    public void shouldAddLike() {
        filmStorage.like(TEST_FILM_TO_LIKE_ID, TEST_USER_ID1);
        Optional<Film> filmOptional = filmStorage.find(TEST_FILM_TO_LIKE_ID);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getLikedFilm());
    }

    static Film getUnlikedFilm() {
        return Film.builder()
                .id(TEST_FILM_ID)
                .name("name1")
                .description("description1")
                .duration(100)
                .mpa(Mpa.builder().id(1L).name(null).build())
                .releaseDate(LocalDate.of(2000, 10, 11))
                .likedUsersIds(new HashSet<>(List.of(2L, 3L, 4L)))
                .build();
    }

    @Test
    @DisplayName("Должен удалять лайк")
    public void shouldRemoveLike() {
        filmStorage.unlike(TEST_FILM_ID, TEST_USER_ID2);
        Optional<Film> filmOptional = filmStorage.find(TEST_FILM_ID);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getUnlikedFilm());
    }

    static Collection<Film> getTestTopFilms() {
        Collection<Film> films = new ArrayList<>();
        films.add(
                Film.builder()
                        .id(1)
                        .name("name1")
                        .description("description1")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 11))
                        .likedUsersIds(new HashSet<>(List.of(1L, 2L, 3L, 4L)))
                        .mpa(Mpa.builder().id(1L).build())
                        .build()
        );

        films.add(
                Film.builder()
                        .id(3)
                        .name("name3")
                        .description("description3")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 13))
                        .likedUsersIds(new HashSet<>(List.of(1L, 2L, 5L)))
                        .mpa(Mpa.builder().id(3L).build())
                        .build()
        );

        films.add(
                Film.builder()
                        .id(5)
                        .name("name5")
                        .description("description5")
                        .duration(100)
                        .releaseDate(LocalDate.of(2000, 10, 15))
                        .likedUsersIds(new HashSet<>(List.of(2L, 3L)))
                        .mpa(Mpa.builder().id(5L).build())
                        .build()
        );
        return films;
    }

    @Test
    @DisplayName("Должен возвратить топ 3 фильмов")
    public void shouldReturnTop3Films() {
        Collection<Film> films = filmStorage.getTopFilms(3L);
        assertThat(films)
                .usingRecursiveComparison()
                .isEqualTo(filmStorage.getTopFilms(3L));
    }
}