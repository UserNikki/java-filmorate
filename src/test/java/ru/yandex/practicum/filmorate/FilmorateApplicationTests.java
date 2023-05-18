package ru.yandex.practicum.filmorate;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Test
    @Order(1)
    public void testCreateUser() {
        User testUser = new User
                ("testlogin", "testname", "email@.ru", LocalDate.of(1980, 12, 12));
        userStorage.create(testUser);
        assertThat(testUser).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(2)
    public void testGetUserById() {
        User user = userStorage.getById(1);
        assertEquals(user.getId(), 1);
        assertThat(user).hasOnlyFields("id", "login", "name", "email", "birthday", "friends");
        assertEquals(user.getId(), 1);
        assertEquals(user.getLogin(), "testlogin");
        assertEquals(user.getName(), "testname");
        assertEquals(user.getEmail(), "email@.ru");
        assertEquals(user.getBirthday(), LocalDate.of(1980, 12, 12));
    }

    @Test
    @Order(3)
    public void updateUser() {
        userStorage.update(new User
                (1, "updatedlogin", "updatedname", "updatedemail@.ru",
                        LocalDate.of(1980, 12, 13)));
        User toCompare = userStorage.getById(1);
        assertEquals(toCompare.getId(), 1);
        assertEquals(toCompare.getLogin(), "updatedlogin");
        assertEquals(toCompare.getName(), "updatedname");
        assertEquals(toCompare.getEmail(), "updatedemail@.ru");
        assertEquals(toCompare.getBirthday(), LocalDate.of(1980, 12, 13));
    }

    @Test
    @Order(4)
    public void getAllUsers() {
        assertEquals(userStorage.getAll().size(), 1);
    }

    @Test
    @Order(5)
    public void addFriendTest() {
        User friend = new User
                ("friendlogin", "friendname", "friendemail@.ru",
                        LocalDate.of(1990, 1, 10));
        userStorage.create(friend);
        userStorage.addFriend(1, 2);
        assertEquals(1, userStorage.allFriends(1).size());
    }

    @Test
    @Order(7)
    public void deleteFriendTest() {
        userStorage.delete(1, 2);
        assertEquals(0, userStorage.getById(1).getFriends().size());
    }

    @Test
    @Order(6)
    public void commonFriendsTest() {
        User anotherOneFriend = new User
                ("Thirdfriendlogin", "Thirdfriendname", "Thirdfriendemail@.ru",
                        LocalDate.of(1999, 1, 10));
        userStorage.create(anotherOneFriend);
        userStorage.addFriend(3, 2);
        List<User> commonFriends = userStorage.commonFriends(1, 3);
        assertEquals(commonFriends.get(0).getId(), 2);
    }

    @Test
    @Order(8)
    public void addFilmTest() {
        Film testFilm = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 100,
                new HashSet<>(), new Mpa(2, "PG"), new LinkedHashSet<>());
        filmStorage.add(testFilm);
        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(9)
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
    @Order(10)
    public void updateFilmTest() {
        filmStorage.update(new Film(1, "UpdatedName", "UpdatedDescription",
                LocalDate.of(2000, 2, 2), 111,
                new HashSet<>(), new Mpa(1, "G"), new LinkedHashSet<>()));
        Film film = filmStorage.getFilm(1);
        assertEquals("UpdatedName", film.getName());
        assertEquals("UpdatedDescription", film.getDescription());
        assertEquals(LocalDate.of(2000, 2, 2), film.getReleaseDate());
        assertEquals(111, film.getDuration());
        assertEquals(1, film.getMpa().getId());
        assertTrue(film.getGenres().isEmpty());
    }

    @Test
    @Order(11)
    public void getAllFilmsTest() {
        assertEquals(1, filmStorage.getAll().size());
    }

    @Test
    @Order(12)
    public void likeFilmTest() {
        filmStorage.like(1, 1);
        Film film = filmStorage.getFilm(1);
        assertEquals(1, film.getLikes().size());
    }

    @Test
    @Order(13)
    public void deleteLikeFilmTest() {
        filmStorage.deleteLike(1, 1);
        Film film = filmStorage.getFilm(1);
        assertTrue(film.getLikes().isEmpty());
    }
}
