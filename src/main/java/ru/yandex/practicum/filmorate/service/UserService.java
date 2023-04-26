package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        if (userStorage.isUserExist(user.getId())) {
            setNameAslogin(user);
            userStorage.update(user);
            log.info("User updated {}", user);
        } else {
            log.info("User with id {} does not exist", user.getId());
            throw new NotFoundException("User does not exist");
        }
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User getUser(int id) {
        return userStorage.getById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void addNewFriend(int id, int friendId) {
        if (userStorage.isUserExist(id) && userStorage.isUserExist(friendId)) {
            userStorage.addFriend(id, friendId);
            log.info("User id {} and user id {} became friends", id, friendId);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public void deleteFriend(int id, int friendId) {
        if (userStorage.isUserExist(id) && userStorage.isUserExist(friendId)) {
            userStorage.delete(id, friendId);
            log.info("User id {} and user id {} are not friends anymore", id, friendId);
        } else {
            throw new NotFoundException("User does not exist");
        }
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.info("User id {} and user id {} looking for common friends", id, otherId);
        return userStorage.commonFriends(id, otherId);
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>(userStorage.allFriends(id));
        if (friends.isEmpty()) {
            log.info("This dude has no friends");
            throw new NotFoundException("Friends list is empty");
        }
        log.info("User id {} friends list requested", id);
        return friends;
    }

    private void setNameAslogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
