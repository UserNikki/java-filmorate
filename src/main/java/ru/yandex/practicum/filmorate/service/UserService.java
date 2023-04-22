package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class UserService {

    //public UserService() {}
    private Integer id = 1;
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        user.setId(generateId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setFriends(new HashSet<>());
        userStorage.create(user);
        log.info("User {} created ", user.getName());
        return user;
    }

    public User updateExistingUser(User user) {
        if (userStorage.isUserExist(user.getId())) {
            user.setFriends(new HashSet<>());
            userStorage.update(user);
            log.info("User {} updated ", user.getName());
        } else {
            log.info("User with id {} does not exist",user.getId());
            throw new NotFoundException("User does not exist");
        }
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User getUser(int id) {
        if (userStorage.getById(id) == null) {
            log.info("User id {} not found",id);
            throw new NotFoundException("User not found");
        }
        return userStorage.getById(id);
    }

    public void addNewFriend(int id, int friendId) {
        if (userStorage.isUserExist(id) && userStorage.isUserExist(friendId)) {
            userStorage.addFriend(id,friendId);
            log.info("User id " + id + " and user id " + friendId + " became friends");
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public void deleteFriend(int id, int friendId) {
        if (userStorage.isUserExist(id) && userStorage.isUserExist(friendId)) {
            userStorage.delete(id,friendId);
            log.info("User id " + id + " and user id " + friendId + " are not friends anymore");
        } else {
            throw new NotFoundException("User does not exist");
        }
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.info("User id " + id + " and user id " + otherId + " looking for common friends");
        return userStorage.commonFriends(id,otherId);
    }

    public List<User> getFriends(int id) {
        if (userStorage.allFriends(id).isEmpty()) {
            log.info("This dude has no friends");
            throw new NotFoundException("Friends list is empty");
        }
        log.info("User id {} friends list requested",id);
        return userStorage.allFriends(id);
    }

    private int generateId() {
        return id++;
    }
}
