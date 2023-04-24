package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        if (filmStorage.isFilmExist(film.getId())) {
            filmStorage.update(film);
            log.info("Film updated: {}", film);
        } else {
            log.info("Film with id {} does not exist", film.getId());
            throw new NotFoundException("Film does not exist");
        }
        return film;
    }

    public Film getFilmById(int id) {
        return Optional.ofNullable(filmStorage.getFilm(id))
                .orElseThrow(() -> new NotFoundException("Film not found"));
    }

    public List<Film> getMostPopularByLike(int quantity) {
        /*только так смог добиться нормальной сортировки
        со сменой порядка.
        в один стрим не заходит это все.
        если знаешь как прям совсем норм сделать, будь добр , подскажи на будущее.
         */
        log.info("The most popular films in the amount of: {}", quantity);
        List<Film> films = filmStorage.getAll().stream()
                .sorted(Comparator.comparing(v -> v.getLikes().size()))
                .collect(Collectors.toList());
        Collections.reverse(films);
        return films.stream().limit(quantity).collect(Collectors.toList());
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
