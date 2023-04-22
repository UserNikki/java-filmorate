package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private Integer id = 1;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addNewFilm(Film film) {
        film.setId(generateId());
        film.setLikes(new HashSet<>());
        filmStorage.add(film);
        log.info("Film {} added ", film.getName());
        return film;
    }

    public Film updateExistingFilm(Film film) {
        if (filmStorage.isFilmExist(film.getId())) {
            film.setLikes(new HashSet<>());
            filmStorage.update(film);
            log.info("Film {} updated", film.getName());
        } else {
            log.info("Film with id {} does not exist", film.getId());
            throw new NotFoundException("Film does not exist");
        }
        return film;
    }

    public Film getFilmById(int id) {
        if (filmStorage.getFilm(id) == null) {
            log.info("Film id {} not found", id);
            throw new NotFoundException("Film not found");
        }
        return filmStorage.getFilm(id);
    }

    public List<Film> getMostPopularByLike(String count) {
        /*ПРИВЕТ.ПОДСКАЖИ ПОЖАЛУЙСТА КАК ОТСОРТИРОВАТЬ.ЕДИНСТВЕННОЕ С ЧЕМ НЕ СПРАВЛЯЮСЬ
         * Я ТАК ПОНЯЛ С ОДИНАКОВЫМ КОЛИЧЕСТВОМ ЛАЙКОВ НЕ ДОБАВЛЯЮТСЯ
         * ПОЭТОМУ ПОСЛЕДНИЙ ТЕСТ НЕ ПРОХОДИТ.МОЖЕТ НУЖЕН ДОП ПАРАМЕТР ДЛЯ СОРТИРОВКИ
         * ЕСЛИ ЛАЙКИ СОВПАДАЮТ ТО ПО ДОП ПАРАМЕТРУ, НО НЕ ЗНАЮ КАК СДЕЛАТЬ
         * ПРОБОВАЛ, НО НЕ ПОЛУЧИЛОСЬ.ВЕСЬ ВЕЧЕР УБИЛ НА ОДИН ЭТОТ МЕТОД
         * */
        final int quantity = Integer.parseInt(count);
        final Comparator<Film> comparator = Comparator.comparing(v -> v.getLikes().size());
        final Set<Film> filmSet = new TreeSet<>(comparator);
        filmSet.addAll(filmStorage.getAll());
        return filmSet.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(ArrayList::new), v -> {
                    Collections.reverse(v);
                    return v.stream().limit(quantity);
                }
        )).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addLike(int id, int userId) {
        filmStorage.like(id, userId);
    }

    public void deleteLike(int id, int userId) {
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getFilms() {
        return filmStorage.getAll();
    }

    private int generateId() {
        return id++;
    }
}
