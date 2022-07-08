package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
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

    public User updateUser (User user) {
        if (userDao.getUsers().containsKey(user.getId())) {
            userDao.updateUser(user);
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь %s не найден", user));
        }
    }

    public boolean addToFriendList(long userId, long friendId) {
        if(userDao.getUsers().containsKey(userId)) {
            if (userDao.getUsers().containsKey(friendId)) {
                userDao.getUsers().get(userId).getFriendList().add(friendId);
                userDao.getUsers().get(friendId).getFriendList().add(userId);
            } else {
                throw new NotFoundException(String.format("Неверно указан идентификатор друга: %s", friendId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с идентификатором %s не найден", userId));
        }
        return true;
    }

    public boolean removeFromFriendList(long userId, long friendId) {
        if(userDao.getUsers().containsKey(userId)) {
            if (userDao.getUsers().containsKey(friendId)) {
                userDao.getUsers().get(userId).getFriendList().remove(friendId);
                userDao.getUsers().get(friendId).getFriendList().remove(userId);
            } else {
                throw new IncorrectParameterException(String.format("неверно указан %s друга", friendId));
            }
        } else {
            throw new IncorrectParameterException(String.format("неверно указан %s пользователя", userId));
        }
        return true;
    }

    public List<User> getListOfMutualFriends(long userId, long friendId) {
        if(userDao.getUsers().containsKey(userId)) {
            if (userDao.getUsers().containsKey(friendId)) {
                List<User> tmpList = new ArrayList<>();
                for (Long id : userDao.getUsers().get(userId).getFriendList()) {
                    if (userDao.getUsers().get(friendId).getFriendList().contains(id)) {
                        tmpList.add(userDao.getUsers().get(id));
                    }
                }
                return tmpList;
            } else {
                throw new IncorrectParameterException(String.format("неверно указан %s друга", friendId));
            }
        } else {
            throw new IncorrectParameterException(String.format("неверно указан %s пользователя", userId));
        }
    }

    public User getUserById(@PathVariable long userId) {
        if (userId <= 0) {
            throw new UserNotFoundException(String.format("Пользователь %s не найден", userId));
        }
        return userDao.getUsers().get(userId);
    }

    public List<User> getFriendsList(long userId) {
        List<User> friendListTmp = new ArrayList<>();
        if(userDao.getUsers().containsKey(userId)) {
            for (Long friendId : userDao.getFriendList(userDao.getUsers().get(userId))) {
                friendListTmp.add(userDao.getUsers().get(friendId));
            }
        } else {
            throw new UserNotFoundException(String.format("Пользователь %s не найден", userId));
        }
        return friendListTmp;
    }
}
