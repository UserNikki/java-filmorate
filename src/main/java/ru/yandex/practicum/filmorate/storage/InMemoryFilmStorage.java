package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> filmStorage = new HashMap<>();
    private Integer id = 1;

    @Override
    public Film add(Film film) {
        film.setId(generateId());
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return Optional.ofNullable(filmStorage.get(id));
    }

    @Override
    public void like(int id, int userId) {
        filmStorage.get(id).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        filmStorage.get(id).getLikes().remove(userId);
    }

    public void isFilmExist(int id) {
        if (!filmStorage.containsKey(id)) {
            throw new NotFoundException("Film does not exist");
        }
    }

    private int generateId() {
        return id++;
    }

}
