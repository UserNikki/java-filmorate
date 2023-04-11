package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private Map<Integer,Film> filmStorage = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (filmStorage.containsKey(film.getId())) {
            log.info("Film with id {} is already exist",film.getId());
            throw new ValidationException("Film is already exist");
        } else {
            log.info("Film {} is added ", film.getName());
            filmStorage.put(film.getId(),film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmStorage.containsKey(id)) {
            log.info("Film {} is updated", film.getName());
            filmStorage.put(id,film);
        } else {
            log.info("Film with id {} does not exist",film.getId());
            throw new ValidationException("Film is already exist");
        }
        return film;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        Film film;
        if (filmStorage.containsKey(id)) {
            film = filmStorage.get(id);
        } else {
            throw new ValidationException("Film with specified id does not exist");
        }
        return film;
    }
}
