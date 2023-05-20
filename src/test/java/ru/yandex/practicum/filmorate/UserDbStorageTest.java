package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTest {
    /*ПРИВЕТ. ВНИМАНИЕ!!!
    ВСЕ ТЕСТЫ КЛАССОВ ХРАНИЛИЩ БАЗЫ ДАННЫХ ЗАПУСКАЮТСЯ РАЗОМ
    ПУТЕМ ЗАПУСКА FilmorateApplicationTests КЛАССА. ПОЗВОЛИЛ СЕБЕ ВОЛЬНОСТЬ ВСЕ ОБЪЕДЕНИТЬ ДЛЯ УДОБСТВА.
     */

    private final UserStorage userStorage;

    @Test
    @Order(1)
    public void testCreateUser() {
        User testUser = new User("testlogin", "testname", "email@.ru", LocalDate.of(1980, 12, 12));
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
        userStorage.update(new User(1, "updatedlogin", "updatedname", "updatedemail@.ru", LocalDate.of(1980, 12, 13)));
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
        User friend = new User("friendlogin", "friendname", "friendemail@.ru", LocalDate.of(1990, 1, 10));
        userStorage.create(friend);
        userStorage.addFriend(1, 2);
        assertEquals(1, userStorage.allFriends(1).size());
    }

    @Test
    @Order(8)
    public void deleteFriendTest() {
        userStorage.delete(1, 2);
        assertEquals(0, userStorage.getById(1).getFriends().size());
    }

    @Test
    @Order(6)
    public void commonFriendsTest() {
        User anotherOneFriend = new User("Thirdfriendlogin", "Thirdfriendname", "Thirdfriendemail@.ru", LocalDate.of(1999, 1, 10));
        userStorage.create(anotherOneFriend);
        userStorage.addFriend(3, 2);
        List<User> commonFriends = userStorage.commonFriends(1, 3);
        assertEquals(commonFriends.get(0).getId(), 2);
    }

    @Test
    @Order(7)
    public void getAllFriendsTest() {
        assertEquals(1, userStorage.allFriends(1).size());
        assertEquals(1, userStorage.allFriends(3).size());
    }

    @Test
    @Order(9)
    public void isUserExistTest() {
        assertTrue(userStorage.isUserExist(1));
        final NotFoundException exp = assertThrows(NotFoundException.class,
                () -> {
                    Boolean test = userStorage.isUserExist(1234);
                }
        );
    }

}