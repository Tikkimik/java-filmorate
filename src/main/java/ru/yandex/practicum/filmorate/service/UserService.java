package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {
    private final UsersDao userDao;
    private final GeneratorId generatorId = new GeneratorId();

    @Autowired
    public UserService(UsersDao userDao) {
        this.userDao = userDao;
    }

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
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
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

    public boolean addToFriendList(long userId, long friendId) throws ValidationException {
        if(userDao.getUsers().containsKey(userId)) {
            if (userDao.getUsers().containsKey(friendId)) {
                userDao.getUsers().get(userId).getFriendList().add(friendId);
                userDao.getUsers().get(friendId).getFriendList().add(userId);
            } else {
                throw new ValidationException("неверно указан id друга");
            }
        } else {
            throw new ValidationException("неверно указан id пользователя");
        }

        return true;
    }

    public void removeFromFriendList() {

    }

    public void getListOfMutualFriends() {

    }


}
