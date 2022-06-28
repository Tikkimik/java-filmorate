package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private final GeneratorId userIdGenerator = new GeneratorId();

    @GetMapping
    public List<User> getFilms() {
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(userIdGenerator.generateId());
        users.add(user);
        return user;
    }

    @PutMapping
    public User reload(@RequestBody User user) {
        users.set(Math.toIntExact(user.getId()-1), user);
        return user;
    }
}
