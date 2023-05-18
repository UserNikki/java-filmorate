package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validators.ReleaseDateValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {

    @Positive
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 1, max = 200, message
            = "Description min: 1 max: 200 characters")
    private String description;
    @ReleaseDateValidator
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Set<Integer> likes, Mpa mpa, LinkedHashSet<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Set<Integer> likes, Mpa mpa, LinkedHashSet<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
    }


    public int getLikesQuantity() {
        return this.likes.size();
    }
}