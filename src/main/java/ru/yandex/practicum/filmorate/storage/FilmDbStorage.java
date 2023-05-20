package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }


    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO films (name,description,releaseDate,duration,mpa_id) VALUES (?,?,?,?,?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, id);
        film.setId(Objects.requireNonNull(id.getKey()).intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?," +
                "description = ?," +
                "releaseDate = ?," +
                "duration = ?," +
                "mpa_id = ?" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film getFilm(int id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, id);
    }

    @Override
    public void like(int id, int userId) {
        String sqlQuery = "INSERT INTO film_likes (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public boolean isFilmExist(int id) {
        String sqlQuery = "SELECT film_id FROM films WHERE film_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!rowSet.next()) {
            throw new NotFoundException("Film id: " + id + " does not exist...");
        }
        return true;
    }

    @Override
    public void createGenreForFilm(Film film) {
        String sqlQueryGenres = "INSERT INTO genres_films (film_id,genre_id) VALUES (?,?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.isGenreExist(genre.getId());
                jdbcTemplate.update(sqlQueryGenres, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void updateGenreForFilm(Film film) {
        String sqlQueryGenres = "DELETE FROM genres_films WHERE film_id = ?";
        jdbcTemplate.update(sqlQueryGenres, film.getId());
        this.createGenreForFilm(film);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    }


    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("mpa_id");
        String sqlQueryMpa = "SELECT * FROM rating WHERE mpa_id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sqlQueryMpa, this::makeMpa, mpaId);
        int filmId = rs.getInt("film_id");
        String sqlQueryGenre = "SELECT * FROM genres WHERE genre_id IN (SELECT genre_id FROM genres_films WHERE film_id = ?)";
        List<Genre> genre = jdbcTemplate.query(sqlQueryGenre, this::makeGenre, filmId);
        String sqlQueryLikes = "SELECT user_id FROM film_likes WHERE film_id = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlQueryLikes, Integer.class, filmId);
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"), new HashSet<>(likes),
                mpa, new LinkedHashSet<>(genre));
    }

}