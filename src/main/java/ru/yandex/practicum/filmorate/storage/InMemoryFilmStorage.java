package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer,Film> filmStorage = new HashMap<>();

    @Override
    public Film add(Film film) {
        filmStorage.put(film.getId(),film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmStorage.put(film.getId(),film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Film getFilm(int id) {
       return filmStorage.get(id);
    }

    @Override
    public void like(int id, int userId) {
        filmStorage.get(id).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        filmStorage.get(id).getLikes().remove(userId);
    }

    public boolean isFilmExist(int id) {
        return filmStorage.containsKey(id);
    }
}
