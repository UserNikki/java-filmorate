package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validators.LoginValidator;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {
    @Positive
    private Integer id;
    @NotBlank
    @LoginValidator
    private String login;
    private String name;
    @Email(message = "Email is not valid")
    @NotBlank
    private String email;
    @PastOrPresent
    @NotNull
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();


    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public User(Integer id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
