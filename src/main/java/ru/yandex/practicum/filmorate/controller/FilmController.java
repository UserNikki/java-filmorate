package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer,Film> filmStorage = new HashMap<>();
    private Integer id = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(generateId());
        log.info("Film {} is added ", film.getName());
        filmStorage.put(film.getId(),film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmStorage.containsKey(film.getId())) {
            log.info("Film {} is updated", film.getName());
            filmStorage.put(film.getId(),film);
        } else {
            log.info("Film with id {} does not exist",film.getId());
            throw new ValidationException("Film is already exist");
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    private int generateId() {
        return id++;
    }

}