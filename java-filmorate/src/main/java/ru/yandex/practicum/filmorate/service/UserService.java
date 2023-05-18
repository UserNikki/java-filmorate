package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;


    public User addUser(User user) {
        setNameAslogin(user);
        userStorage.create(user);
        log.info("User created {}:", user);
        return user;
    }

    public User updateExistingUser(User user) {
        setNameAslogin(user);
        userStorage.isUserExist(user.getId());
        userStorage.update(user);
        log.info("User updated {}", user);
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User getUser(int id) {
        userStorage.isUserExist(id);
        return userStorage.getById(id);
    }

    public void addNewFriend(int id, int friendId) {
        userStorage.isUserExist(id);
        userStorage.isUserExist(friendId);
        userStorage.addFriend(id, friendId);
        log.info("User id {} and user id {} became friends", id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.isUserExist(id);
        userStorage.isUserExist(friendId);
        userStorage.delete(id, friendId);
        log.info("User id {} and user id {} are not friends anymore", id, friendId);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.info("User id {} and user id {} looking for common friends", id, otherId);
        return userStorage.commonFriends(id, otherId);
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>(userStorage.allFriends(id));
        log.info("User id {} friends list requested", id);
        return friends;
    }

    private void setNameAslogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
