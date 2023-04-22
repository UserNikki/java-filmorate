package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LoginValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {

    @Positive
    private Integer id;
    @NotEmpty
    @LoginValidator
    private String login;
    private String name;
    @Email(message = "Email is not valid")
    @NotEmpty
    private String email;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
