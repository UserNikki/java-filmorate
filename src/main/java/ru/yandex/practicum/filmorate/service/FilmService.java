package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final MpaStorage mpaStorage;

    public Film addNewFilm(Film film) {
        mpaStorage.isMpaExist(film.getMpa().getId());
        filmStorage.add(film);
        filmStorage.createGenreForFilm(film);
        log.info("Film added: {}", film);
        return film;
    }

    public Film updateExistingFilm(Film film) {
        filmStorage.isFilmExist(film.getId());
        filmStorage.updateGenreForFilm(film);
        mpaStorage.isMpaExist(film.getMpa().getId());
        filmStorage.update(film);
        log.info("Film updated: {}", film);
        return film;
    }

    public Film getFilmById(int id) {
        filmStorage.isFilmExist(id);
        return filmStorage.getFilm(id);
    }

    public List<Film> getMostPopularByLike(int quantity) {
        log.info("The most popular films in the amount of: {}", quantity);
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(Film::getLikesQuantity).reversed())
                .limit(quantity).collect(Collectors.toList());
    }

    public void addLike(int id, int userId) {
        filmStorage.isFilmExist(id);
        userStorage.isUserExist(userId);
        filmStorage.like(id, userId);
        log.info("Film id: {} like from user: {} ", id, userId);
    }

    public void deleteLike(int id, int userId) {
        if (getFilmById(id).getLikes().contains(userId)) {
            filmStorage.deleteLike(id, userId);
            log.info("Film id: {} minus like from user: {} ", id, userId);
        } else {
            throw new NotFoundException("Film does not contain like from user id: " + userId);
        }
    }

    public List<Film> getFilms() {
        log.info("Get all films service method");
        return filmStorage.getAll();
    }

}
