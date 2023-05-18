package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<LoginValidator,String> {

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !login.contains(" ");
    }
}
