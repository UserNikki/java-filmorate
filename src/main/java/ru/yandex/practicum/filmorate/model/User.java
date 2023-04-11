package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

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

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = UserLoginValidator.class)
    public @interface LoginValidator {
        String message() default "login must not contain spaces";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
    public static class UserLoginValidator implements ConstraintValidator<LoginValidator,String> {

        @Override
        public boolean isValid(String login, ConstraintValidatorContext context) {
            return !login.contains(" ");
        }
    }
}
