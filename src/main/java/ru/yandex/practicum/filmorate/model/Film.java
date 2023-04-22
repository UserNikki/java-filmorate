package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ReleaseDateValidator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
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
    private Set<Integer> likes;

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}