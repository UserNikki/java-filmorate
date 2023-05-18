package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film getFilm(int id);

    void like(int id, int userId);

    void deleteLike(int id, int userId);

    void isFilmExist(int id);

    List<Genre> getAllGenres();

    Genre getGenreById(int id);

    void createGenreForFilm(Film film);

    void updateGenreForFilm(Film film);

    List<Mpa> getAllMpa();

    Mpa getMpaById(int id);

    void isGenreExist(int id);

    void isMpaExist(int id);
}
