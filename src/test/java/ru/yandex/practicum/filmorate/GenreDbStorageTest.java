package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    /*ПРИВЕТ. ВНИМАНИЕ!!!
    ВСЕ ТЕСТЫ КЛАССОВ ХРАНИЛИЩ БАЗЫ ДАННЫХ ЗАПУСКАЮТСЯ РАЗОМ
    ПУТЕМ ЗАПУСКА FilmorateApplicationTests КЛАССА. ПОЗВОЛИЛ СЕБЕ ВОЛЬНОСТЬ ВСЕ ОБЪЕДЕНИТЬ ДЛЯ УДОБСТВА.
     */

    private final GenreStorage genreStorage;

    @Test
    public void getAllGenresTest() {
        List<Genre> genres = genreStorage.getAllGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
        assertEquals(6, genres.size());
        assertEquals(1, genres.get(0).getId());
        assertEquals("Комедия", genres.get(0).getName());
        assertEquals(2, genres.get(1).getId());
        assertEquals("Драма", genres.get(1).getName());
        assertEquals(3, genres.get(2).getId());
        assertEquals("Мультфильм", genres.get(2).getName());
        assertEquals(4, genres.get(3).getId());
        assertEquals("Триллер", genres.get(3).getName());
        assertEquals(5, genres.get(4).getId());
        assertEquals("Документальный", genres.get(4).getName());
        assertEquals(6, genres.get(5).getId());
        assertEquals("Боевик", genres.get(5).getName());
    }

    @Test
    public void getGenreByIdTest() {
        assertEquals("Комедия", genreStorage.getGenreById(1).getName());
    }

    @Test
    public void isGenreExistTest() {
        assertTrue(genreStorage.isGenreExist(1));
        final NotFoundException exp = assertThrows(NotFoundException.class,
                () -> {
                    Boolean test = genreStorage.isGenreExist(1234);
                }
        );
    }
}