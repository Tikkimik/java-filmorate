package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {

    private final GeneratorId generatorId = new GeneratorId();

    @Autowired
    private UsersDao userDao;

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public ResponseEntity<User> addUser(User user) {
        if (user.getLogin() != null) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(generatorId.generateId());
            userDao.addUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> updateUser (User user) {
        if (userDao.getUsers().containsKey(user.getId())) {
            userDao.updateUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
        }
    }
}