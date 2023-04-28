package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> userStorage = new HashMap<>();
    private Integer id = 1;

    @Override
    public User create(User user) {
        user.setId(generateId());
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    @Override
    public void addFriend(int id, int friendId) {
        userStorage.get(id).getFriends().add(friendId);
        userStorage.get(friendId).getFriends().add(id);
    }

    @Override
    public void delete(int id, int friendId) {
        userStorage.get(id).getFriends().remove(friendId);
        userStorage.get(friendId).getFriends().remove(id);
    }

    @Override
    public List<User> commonFriends(int id, int otherId) {
        Set<Integer> firstFriend = userStorage.get(id).getFriends();
        Set<Integer> secondFriend = userStorage.get(otherId).getFriends();
        return firstFriend.stream()
                .filter(secondFriend::contains)
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> allFriends(int id) {
        return userStorage.get(id).getFriends().stream()
                .map(userStorage::get).collect(Collectors.toList());
    }

    public void isUserExist(int id) {
        if (!userStorage.containsKey(id)) {
            throw new NotFoundException("User does not exist");
        }
    }

    private int generateId() {
        return id++;
    }

}
