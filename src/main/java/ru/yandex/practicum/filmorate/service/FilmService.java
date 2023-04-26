package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film addNewFilm(Film film) {
        filmStorage.add(film);
        log.info("Film added: {}", film);
        return film;
    }

    public Film updateExistingFilm(Film film) {
        filmStorage.isFilmExist(film.getId());
        filmStorage.update(film);
        log.info("Film updated: {}", film);
        return film;
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    public List<Film> getMostPopularByLike(int quantity) {
        log.info("The most popular films in the amount of: {}", quantity);
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(quantity).collect(Collectors.toList());
    }

    public void addLike(int id, int userId) {
        log.info("Film id: {} like from user: {} ", id, userId);
        filmStorage.like(id, userId);
    }

    public void deleteLike(int id, int userId) {
        log.info("Film id: {} minus like from user: {} ", id, userId);
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getFilms() {
        return filmStorage.getAll();
    }
}
