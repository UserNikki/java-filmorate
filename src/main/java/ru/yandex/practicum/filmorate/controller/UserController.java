package ru.yandex.practicum.filmorate.controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private final Map<Integer,User> userStorage = new HashMap<>();
    private Integer id = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
            user.setId(generateId());
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            log.info("User {} is created ", user.getName());
            userStorage.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (userStorage.containsKey(user.getId())) {
            log.info("User {} is updated ", user.getName());
            userStorage.put(user.getId(),user);
        } else {
            log.info("User with id {} does not exist",user.getId());
            throw new ValidationException("User does not exist");
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    private int generateId() {
        return id++;
    }
}