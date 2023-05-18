package ru.yandex.practicum.filmorate.validationtest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmValidationTest {
    Film correctDataFilm;
    FilmController controller;
    private Validator validator;
    private FilmService filmService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private UserService userService;
    private JdbcTemplate jdbcTemplate;
    private EmbeddedDatabase embeddedDatabase;


    @BeforeEach
    void createTestData() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
        this.correctDataFilm = new Film("Die Hard", "Bruce Willis is killing bad guys", LocalDate.of(1990, 11, 11), 90, new HashSet<>(), new Mpa(1, "G"), new LinkedHashSet<>());
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        this.jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        this.userStorage = new UserDbStorage(this.jdbcTemplate);
        this.userService = new UserService(this.userStorage);
        this.filmStorage = new FilmDbStorage(this.jdbcTemplate);
        this.filmService = new FilmService(this.filmStorage, this.userStorage);
        this.controller = new FilmController(filmService);
    }

    @Test
    void shouldAddFilmWhenCorrectData() {
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenDescriptionIsMoreThan200CharsLong() {
        try {
            char[] longDescription = new char[201];
            Arrays.fill(longDescription, 'i');
            correctDataFilm.setDescription(new String(longDescription));
            controller.addFilm(correctDataFilm);
            Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
            assertEquals(1, violations.size());
        } catch (DataIntegrityViolationException e) {
            e.getStackTrace();
        }
    }

    @Test
    void shouldAddFilmWhenDescriptionIs200CharsLong() {
        char[] longDescription = new char[200];
        Arrays.fill(longDescription, 'i');
        correctDataFilm.setDescription(new String(longDescription));
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIsNegativeTest() {
        controller.addFilm(correctDataFilm);
        correctDataFilm.setId(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIs0Test() {
        controller.addFilm(correctDataFilm);
        correctDataFilm.setId(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenNameIsEmptyTest() {
        correctDataFilm.setName("");
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenReleaseDateIsBefore28December1895Test() {
        correctDataFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldAddFilmWhenReleaseDateIs28December1895Test() {
        correctDataFilm.setReleaseDate(LocalDate.of(1895, 12, 28));
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenDurationIsNegativeTest() {
        correctDataFilm.setDuration(-1);
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenDurationIs0Test() {
        correctDataFilm.setDuration(0);
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
    }
}