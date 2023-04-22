package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> userStorage = new HashMap<>();

    @Override
    public User create(User user) {
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
        if (isUserExist(id)) {
            return userStorage.get(id);
        }
        return null;
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
    public List<User> commonFriends(int id, int otherId) {
        Set<Integer> firstFriend = userStorage.get(id).getFriends();
        Set<Integer> secondFriend = userStorage.get(otherId).getFriends();
        List<Integer> commonFriendsId = firstFriend.stream()
                .filter(secondFriend::contains)
                .collect(Collectors.toList());
        List<User> friendsList = new ArrayList<>();
        if (commonFriendsId.isEmpty()) {
            return Collections.emptyList();
        } else {
            for (Integer i : commonFriendsId) {
                friendsList.add(userStorage.get(i));
            }
        }
        return friendsList;
    }

    @Override
    public List<User> allFriends(int id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.get(id).getFriends()) {
            friends.add(userStorage.get(friendId));
        }
        return friends;
    }

    public boolean isUserExist(int id) {
        return userStorage.containsKey(id);
    }

}
