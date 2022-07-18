package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.dao.interfaces.UserDao;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.*;

@Component("UserDaoImpl")
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    SqlRowSet sqlRowSet;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (sqlRowSet.next()) {
            users.add(new User(sqlRowSet.getLong("USER_ID"),
                    sqlRowSet.getString("EMAIL"),
                    sqlRowSet.getString("LOGIN"),
                    Objects.requireNonNull(sqlRowSet.getString("NAME")),
                    Objects.requireNonNull(sqlRowSet.getDate("BIRTHDAY")).toLocalDate()
            ));
        }
        return users;
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> users= new HashMap<>();
        sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (sqlRowSet.next()) {
            users.put(sqlRowSet.getLong("USER_ID"), new User(sqlRowSet.getLong("USER_ID"),
                    sqlRowSet.getString("EMAIL"),
                    sqlRowSet.getString("LOGIN"),
                    Objects.requireNonNull(sqlRowSet.getString("NAME")),
                    Objects.requireNonNull(sqlRowSet.getDate("BIRTHDAY")).toLocalDate()
            ));
        }
        return users;
    }

    @Override
    public User getUserById(long userId) {
        SqlRowSet friends = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)", userId);
        Set<Long> friend = new TreeSet<>();
        if (friends.next()) {
            do {
                friend.add(friends.getLong("USER_ID"));
            } while (friends.next());
        }

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE user_id = ?", userId);
        if (sqlRowSet.next()) {
            return new User(sqlRowSet.getLong("USER_ID"),
                    sqlRowSet.getString("EMAIL"),
                    sqlRowSet.getString("LOGIN"),
                    Objects.requireNonNull(sqlRowSet.getString("NAME")),
                    Objects.requireNonNull(sqlRowSet.getDate("BIRTHDAY")).toLocalDate(),
                    friend);
        }
        return null;
    }

    @Override
    public List<User> getFriendsList(long userId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)", userId);

        while (sqlRowSet.next()) {
            friends.add(new User(sqlRowSet.getLong("USER_ID"),
                    sqlRowSet.getString("EMAIL"),
                    sqlRowSet.getString("LOGIN"),
                    Objects.requireNonNull(sqlRowSet.getString("NAME")),
                    Objects.requireNonNull(sqlRowSet.getDate("BIRTHDAY")).toLocalDate()
            ));
        }
        return friends;
    }

    @Override
    public List<User> getListOfMutualFriends(long id, long otherId) {
        return jdbcTemplate.query(
            "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = " + id
            + ") AND USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = " + otherId + ")"
            , (sqlRowSet, rowNum) -> new User(
                sqlRowSet.getLong("USER_ID"),
                sqlRowSet.getString("EMAIL"),
                sqlRowSet.getString("LOGIN"),
                sqlRowSet.getString("NAME"),
                sqlRowSet.getDate("BIRTHDAY").toLocalDate()
            )
        );
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(this.userConvert(user)).longValue());
        insertFriends(user);
        return user;
    }

    public Map<String, Object> userConvert(User user) {
        return parameters(user);
    }

    public static Map<String, Object> parameters(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(obj)); } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return map;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?",
                                 user.getEmail(),user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        removeFromFriendList(user);
        insertFriends(user);
        return user;
    }

    private void removeFromFriendList(User user) {
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ?", user.getId());
    }

    private void insertFriends(User user) {
        if (user.getFriendList().isEmpty()) { return; }

        try (
            PreparedStatement preparedStatement = Objects.requireNonNull(jdbcTemplate.getDataSource())
            .getConnection()
            .prepareStatement("INSERT INTO FRIENDS (FRIEND_ID, USER_ID) VALUES (?, ?)")
        ) {
            for (Long friend : user.getFriendList()) {
                preparedStatement.setLong(1, friend);
                preparedStatement.setLong(2, user.getId());
                preparedStatement.addBatch();
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
