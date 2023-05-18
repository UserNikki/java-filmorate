package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<ReleaseDateValidator, LocalDate> {
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 27);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date != null && date.isAfter(EARLIEST_DATE);
    }
}
