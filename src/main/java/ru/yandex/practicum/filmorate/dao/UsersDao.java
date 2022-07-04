package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UsersDao {
    private final Map<Long, User> users = new HashMap<>();

    public Map<Long, User> getUsers() {
        return users;
    }

    public List<User> getAll() {
        return users.values().stream().collect(Collectors.toList());
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }
}
