package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LoginValidator;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    /*
    Я НЕ В ТУТ ВЕТКУ В ПРОШЛЫ РАЗ ЗАГНАЛ
    ВОТ ССЫЛКА НА СТАРЫЙ ПУЛ РЕКВЕСТ
    https://github.com/UserNikki/java-filmorate/pull/6

    у нас косяк в том, что изначально я писал валидацию исходя из необходимости отсечь невалидное
    сразу на входе т.е. так как ты и советуешь. но тесты постман встроенные практикумом в пул реквест прпдыдущего спринта
     не были не это расчитаны и не проходили. пришлось тогда переписать
    под тесты и пропускать нал значения.
    сейчас тесты постман слава богу в гит не встроены))) а то капец.
    вот с таким телом уже не создается из за null в id и все тесты по цепочке падают
    я поэтому оставил обертку вместо примитива и не ставлю нотнал.
    {
  "login": "dolore",
  "name": "Nick Name",
  "email": "mail@mail.ru",
  "birthday": "1946-08-20"
}
А так, сам тестил, все работает.
     */

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
}
