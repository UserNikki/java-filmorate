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
    public User getById(int id) {
        /*не совсем понял, так?
        или в сигнатуру прям загнать Optional<Film> ???
        а нужно это во всех слоях делать?
        или здесь Optional<Film>, а в сервисе просто Film?
        или и там и тут одинаково...хм
         */
        return Optional.ofNullable(userStorage.get(id))
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public void addFriend(int id, int friendId) {
        System.out.println("размер до" + userStorage.get(id).getFriends().size());
        userStorage.get(id).getFriends().add(friendId);
        userStorage.get(friendId).getFriends().add(id);
        System.out.println("размер после" + userStorage.get(id).getFriends().size());
    }

    @Override
    public void delete(int id, int friendId) {
        userStorage.get(id).getFriends().remove(friendId);
        userStorage.get(friendId).getFriends().remove(id);
    }

    @Override
    public List<User> commonFriends(int id, int otherId) {//спасибо, так красивее намного
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

    public boolean isUserExist(int id) {
        return userStorage.containsKey(id);
    }

    private int generateId() {
        return id++;
    }

}
