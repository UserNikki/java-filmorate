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
    /* ПРИВЕТ. РЕШИЛ ВЫПОЛНИТЬ С ЗАДАНИЕМ СО * , НО Т.К НАМ НИЧЕГО В ТЕОРИИ ТОЛКОМ НЕ ОБЪЯСНИЛИ О ПРОЦЕССАХ ПОД КАПОТОМ
     ПРИШЛОСЬ САМОМУ СООБРАЖАТЬ С ВАЛИДАЦИЕЙ И
    ПИСАТЬ СВОИ АННОТАЦИИ Т.К. НАПИСАНИЕ ОБЫЧНОГО КОДА ДЛЯ ВАЛИДАЦИИ РУШИТ КРАСОТУ)
     ВСЕ РАВНО ДО КОНЦА НЕ РАЗОБРАЛСЯ ПОКА, НО ВСЕ РАБОТАЕТ.

     */

    private final Map<Integer,User> userStorage = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        try {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            if (userStorage.containsKey(user.getId())) {
                log.info("User with id {} is already exist",user.getId());
                throw new ValidationException("User is already exist");
            } else {
                userStorage.put(user.getId(), user);
            }
        }
        catch (NullPointerException exp) {
            exp.getStackTrace();
        }
        return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@Valid @RequestBody User user, @PathVariable int id) {
        if (userStorage.containsKey(id)) {
            log.info("User {} is updated ", user.getName());
            userStorage.put(id,user);
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

}
