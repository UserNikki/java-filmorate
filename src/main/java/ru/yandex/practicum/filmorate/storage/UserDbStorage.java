package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    /*Привет. Очень прошу по возможности основные замечания постараться сразу дать, чтобы за пару
    проверок сдать. Мне просто отъехать надо будет на днях.
     */

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users (login,name,email,birthday) VALUES (?,?,?,?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, (java.sql.Date.valueOf(user.getBirthday())));
            return ps;
        }, id);
        user.setId(Objects.requireNonNull(id.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "login = ?," +
                "name = ?," +
                "email = ?," +
                "birthday = ?" +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public void isUserExist(int id) {
        String sqlQuery = "SELECT user_id FROM users WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!rowSet.next()) {
            throw new NotFoundException("User id: " + id + " does not exist...");
        }
    }

    @Override
    public User getById(int id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id,friend_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void delete(int id, int friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> commonFriends(int id, int friendId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id IN " +
                "(SELECT friend_id FROM friendship WHERE user_id = ?) " +
                "AND user_id IN (SELECT friend_id FROM friendship WHERE user_id = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id, friendId);
        List<User> commonFriends = new ArrayList<>();
        while (rs.next()) {
            commonFriends.add(new User(rs.getInt("user_id"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("email"),
                    Objects.requireNonNull(rs.getDate("birthday")).toLocalDate()));
        }
        return commonFriends.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<User> allFriends(int id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id IN " +
                "(SELECT friend_id FROM friendship WHERE user_id = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        List<User> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(new User(rs.getInt("user_id"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("email"),
                    Objects.requireNonNull(rs.getDate("birthday")).toLocalDate()));
        }
        return friends;
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate());
    }


}