package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film handler");
        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film handler");
        return filmService.updateExistingFilm(film);
    }

    @GetMapping("/{id}")
    public Film getUserById(@PathVariable int id) {
        log.info("Get film by id handler");
        return filmService.getFilmById(id);
        }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable int id, @PathVariable int userId) {
        log.info("Like handler");
        filmService.addLike(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Delete like handler");
        filmService.deleteLike(id,userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilm(@RequestParam(required = false) String count) {
        log.info("Most popular by like quantity handler");
        return filmService.getMostPopularByLike(Objects.requireNonNullElse(count, "10"));
        //подскажи плиз, можно ли здесь указать если параметра каунт нету то по дефолту 10
        //но не через перезапись переменной, а чтобы оно само делалось
        //чтобы не создавать отдельный эндпойнт как ниже. пробовал кое-что, но не сработало
    }

    /*@GetMapping("/popular")
    public List<Film> mostLikedFilmDefaultQuantity() {
        log.info("Most popular by like default quantity");
        return filmService.getMostPopularByLike(10);
    }*/

}