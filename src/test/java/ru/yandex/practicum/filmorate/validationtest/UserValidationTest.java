package ru.yandex.practicum.filmorate.validationtest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    /*ТЕСТЫ ПИСАЛ ИСХОДЯ ИЗ ТОГО, ЧТО ТЕСТИРУЕМ ВНУТРЕННЮЮ РЕАЛИЗАЦИЮ
     ВАЛИДАЦИИ ЧЕРЕЗ ЗАПРОСЫ К СЕРВЕРУ. Я ТАК ПОНЯЛ, ЧТО СМЫСЛ В ЭТОМ
     */
    User correctUser;
    UserController controller;
    private Validator validator;

    @BeforeEach
    void createTestData() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
        this.correctUser = new User(555,"qwerty@mail.com","login","Cool", LocalDate.of(2000,11,11));
        this.controller = new UserController();
    }

    @Test
    void shouldCreateUserWhenCorrectData() {
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldSetNameAsLoginWhenEmptyName() {
        correctUser.setName("");
        controller.createUser(correctUser);
        assertEquals(correctUser.getName(), correctUser.getLogin());
    }

    @Test
    void shouldReturnValidationErrorWhenNameIsNull() {
        correctUser.setName(null);
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIsNegativeTest() {
        correctUser.setId(-1);
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIs0Test() {
        correctUser.setId(0);
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIsNullTest() {
        correctUser.setId(null);
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenLackOfAtSignEmailTest() {
        correctUser.setEmail("qwerty.mail");
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenEmailIsEmptyTest() {
        correctUser.setEmail("");
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenLoginContainsWhitespaceTest() {
        correctUser.setLogin("login login");
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenLoginIsEmptyTest() {
        correctUser.setLogin("");
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenBirthDayIsTodayTest() {
        correctUser.setBirthday(LocalDate.now());
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenBirthDayIsTomorrowTest() {
        correctUser.setBirthday(LocalDate.now().plusDays(1));
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldBeOkWhenBirthDayIsYesterdayTest() {
        correctUser.setBirthday(LocalDate.now().minusDays(1));
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(0, violations.size());
    }

}
