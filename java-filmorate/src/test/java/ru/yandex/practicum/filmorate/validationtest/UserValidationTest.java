package ru.yandex.practicum.filmorate.validationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class UserValidationTest {
    User correctUser;
    UserController controller;
    private Validator validator;
    private UserStorage userStorage;
    private UserService userService;
    private JdbcTemplate jdbcTemplate;
    private EmbeddedDatabase embeddedDatabase;


    @BeforeEach
    void createTestData() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }

        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        this.jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        this.userStorage = new UserDbStorage(this.jdbcTemplate);
        this.userService = new UserService(this.userStorage);
        this.correctUser = new User(555, "login", "name", "qwerty@mail.ru", LocalDate.of(1990, 1, 1));
        this.controller = new UserController(this.userService);
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
    void shouldSetNameWhenNameIsNull() {
        correctUser.setName(null);
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(0, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIsNegativeTest() {
        controller.createUser(correctUser);
        correctUser.setId(-1);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldReturnValidationErrorWhenIdIs0Test() {
        controller.createUser(correctUser);
        correctUser.setId(0);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldSetIdWhenIdIsNullTest() {
        correctUser.setId(null);
        controller.createUser(correctUser);
        Set<ConstraintViolation<User>> violations = validator.validate(correctUser);
        assertEquals(0, violations.size());
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