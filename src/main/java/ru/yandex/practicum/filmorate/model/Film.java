package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {

    @Positive
    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Size(min = 1, max = 200, message
            = "Description has to be between 1 and 200 characters")
    private String description;
    @ReleaseDateValidator
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = FilmReleaseDateValidator.class)
    public @interface ReleaseDateValidator {
        String message() default "date is too old";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class FilmReleaseDateValidator implements ConstraintValidator<ReleaseDateValidator,LocalDate> {
        private static final LocalDate EARLIEST_DATE = LocalDate.of(1895,12,27);

        @Override
        public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
            return date.isAfter(EARLIEST_DATE);
        }
    }
}
