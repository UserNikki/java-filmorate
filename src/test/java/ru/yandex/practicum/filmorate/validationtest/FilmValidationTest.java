package ru.yandex.practicum.filmorate.validationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmValidationTest {
    Film correctDataFilm;
    FilmController controller;
    private Validator validator;


    @BeforeEach
    void createTestData() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
        this.correctDataFilm = new Film("Die Hard", "Bruce Willis is killing bad guys", LocalDate.of(1990, 11, 11), 90);
        this.controller = new FilmController(new FilmService(new InMemoryFilmStorage(),new InMemoryUserStorage()));
    }

    @Test
    void shouldAddFilmWhenCorrectData() {
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenDescriptionIsMoreThan200CharsLong() {
        char[] longDescription = new char[201];
        Arrays.fill(longDescription, 'i');
        correctDataFilm.setDescription(new String(longDescription));
        controller.addFilm(correctDataFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(correctDataFilm);
        assertEquals(1, violations.size());
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