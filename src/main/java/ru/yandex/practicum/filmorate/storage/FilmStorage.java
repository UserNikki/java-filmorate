package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    List<Film> getAll();

    Optional<Film> getFilm(int id);

    void like(int id, int userId);

    void deleteLike(int id, int userId);

    void isFilmExist(int id);
}
