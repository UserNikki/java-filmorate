package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);
    User update(User user);
    List<User> getAll();
    boolean isUserExist(int id);
    User getById(int id);
    void addFriend(int id, int friendId);
    void delete(int id, int friendId);
    List<User> commonFriends(int id, int otherId);
    List<User> allFriends(int id);

}
