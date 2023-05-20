package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmDbStorageTest {
    /*ПРИВЕТ. ВНИМАНИЕ!!!
    ВСЕ ТЕСТЫ КЛАССОВ ХРАНИЛИЩ БАЗЫ ДАННЫХ ЗАПУСКАЮТСЯ РАЗОМ
    ПУТЕМ ЗАПУСКА FilmorateApplicationTests КЛАССА. ПОЗВОЛИЛ СЕБЕ ВОЛЬНОСТЬ ВСЕ ОБЪЕДЕНИТЬ ДЛЯ УДОБСТВА.
     */

    private final FilmStorage filmStorage;

    @Test
    @Order(1)
    public void addFilmTest() {
        Film testFilm = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 100, new HashSet<>(), new Mpa(2, "PG"), new LinkedHashSet<>());
        filmStorage.add(testFilm);
        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(2)
    public void getFilmByIdTest() {
        Film film = filmStorage.getFilm(1);
        assertEquals(film.getId(), 1);
        assertThat(film).hasOnlyFields("id", "name", "description", "releaseDate", "duration", "likes", "mpa", "genres");
        assertEquals("Name", film.getName());
        assertEquals("Description", film.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), film.getReleaseDate());
        assertEquals(100, film.getDuration());
        assertEquals(2, film.getMpa().getId());
        assertTrue(film.getGenres().isEmpty());
        assertTrue(film.getLikes().isEmpty());
    }

    @Test
    @Order(3)
    public void updateFilmTest() {
        filmStorage.update(new Film(1, "UpdatedName", "UpdatedDescription", LocalDate.of(2000, 2, 2), 111, new HashSet<>(), new Mpa(1, "G"), new LinkedHashSet<>()));
        Film film = filmStorage.getFilm(1);
        assertEquals("UpdatedName", film.getName());
        assertEquals("UpdatedDescription", film.getDescription());
        assertEquals(LocalDate.of(2000, 2, 2), film.getReleaseDate());
        assertEquals(111, film.getDuration());
        assertEquals(1, film.getMpa().getId());
        assertTrue(film.getGenres().isEmpty());
    }

    @Test
    @Order(4)
    public void getAllFilmsTest() {
        assertEquals(1, filmStorage.getAll().size());
    }

    @Test
    @Order(5)
    public void likeFilmTest() {
        filmStorage.like(1, 1);
        Film film = filmStorage.getFilm(1);
        assertEquals(1, film.getLikes().size());
    }

    @Test
    @Order(6)
    public void deleteLikeFilmTest() {
        filmStorage.deleteLike(1, 1);
        Film film = filmStorage.getFilm(1);
        assertTrue(film.getLikes().isEmpty());
    }

    @Test
    @Order(7)
    public void createGenreForFilmTest() {
        Film film = filmStorage.getFilm(1);
        film.getGenres().add(new Genre(1, "Комедия"));
        filmStorage.add(film);
        filmStorage.createGenreForFilm(film);
        assertEquals(1, filmStorage.getFilm(2).getGenres().size());
        List<Genre> genres = new ArrayList<>(filmStorage.getFilm(2).getGenres());
        assertThat(genres.get(0)).hasOnlyFields("id", "name");
        assertEquals("Комедия", genres.get(0).getName());
        assertEquals(1, genres.get(0).getId());
    }

    @Test
    @Order(8)
    public void updateGenreForFilmTest() {
        Film film = filmStorage.getFilm(2);
        film.getGenres().add(new Genre(2, "Драма"));
        filmStorage.update(film);
        filmStorage.updateGenreForFilm(film);
        assertEquals(2, filmStorage.getFilm(2).getGenres().size());
        List<Genre> genres = new ArrayList<>(filmStorage.getFilm(2).getGenres());
        assertThat(genres.get(0)).hasOnlyFields("id", "name");
        assertEquals("Комедия", genres.get(0).getName());
        assertEquals(1, genres.get(0).getId());
        assertThat(genres.get(1)).hasOnlyFields("id", "name");
        assertEquals("Драма", genres.get(1).getName());
        assertEquals(2, genres.get(1).getId());
    }

    @Test
    @Order(9)
    public void isFilmExistTest() {
        assertTrue(filmStorage.isFilmExist(1));
        final NotFoundException exp = assertThrows(NotFoundException.class,
                () -> {
                    Boolean test = filmStorage.isFilmExist(1234);
                }
        );
    }


}