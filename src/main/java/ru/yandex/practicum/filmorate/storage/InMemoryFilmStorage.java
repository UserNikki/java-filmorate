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
    public Film getFilm(int id) {
        /*не совсем понял, так?
        или в сигнатуру прям загнать Optional<Film> ???
        а нужно это во всех слоях делать?
        или здесь Optional<Film>, а в сервисе просто Film?
        или и там и тут одинаково...хм
         */
        return Optional.ofNullable(filmStorage.get(id))
                .orElseThrow(() -> new NotFoundException("Film not found"));
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

    private int generateId() {
        return id++;
    }

}
