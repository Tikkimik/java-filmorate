package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.stream.Collectors;

public interface UserStorage {

    List<User> getAll();

    void addUser(User user);

    void updateUser(User user);
}
