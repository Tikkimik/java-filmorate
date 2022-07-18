package ru.yandex.practicum.filmorate.service.user;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.dao.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao dao;  //реализуем через интерфейс, для удобной подмены реализации

    @Autowired
    public UserServiceImpl(@Qualifier("UserDaoImpl") UserDao dao) { //внедряем бин UserDaoImpl
        this.dao = dao;
    }

    @Override
    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    @Override
    public User getUserById(@PathVariable long userId) {
        if (userId <= 0) {
            throw new UserNotFoundException(String.format("Пользователь %s не найден", userId));
        }
        return dao.getUserById(userId);
    }

    @Override
    public List<User> getFriendsList(long id) {
        return dao.getFriendsList(id);
    }

    @Override
    public List<User> getListOfMutualFriends(long userId, long friendId) {
        if(dao.getUsers().containsKey(userId)) {
            if (dao.getUsers().containsKey(friendId)) {
                return dao.getListOfMutualFriends(userId, friendId);
            } else {
                throw new IncorrectParameterException(String.format("неверно указан %s друга", friendId));
            }
        } else {
            throw new IncorrectParameterException(String.format("неверно указан %s пользователя", userId));
        }
    }

    @Override
    public User addUser(User user) {
        if (user.getLogin() != null) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return dao.addUser(user);
        } else {
            throw new RuntimeException("что-то пошло не так");
        }
    }

    @Override
    public User updateUser (User user) {
        if (dao.getUsers().containsKey(user.getId())) {
            dao.updateUser(user);
            return dao.updateUser(user);
        } else {
            throw new UserNotFoundException(String.format("Пользователь %s не найден", user));
        }
    }

    @Override
    public boolean addToFriendList(long userId, long friendId) {
        if(dao.getUsers().containsKey(userId)) {
            if (dao.getUsers().containsKey(friendId)) {
                User user = dao.getUserById(userId);
                user.getFriendList().add(friendId);
                dao.updateUser(user);
            } else {
                throw new NotFoundException(String.format("Неверно указан идентификатор друга: %s", friendId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с идентификатором %s не найден", userId));
        }
        return true;
    }

    public boolean removeFromFriendList(long userId, long friendId) {
        if(dao.getUsers().containsKey(userId)) {
            if (dao.getUsers().containsKey(friendId)) {
                dao.getUsers().get(userId).getFriendList().remove(friendId);
                dao.getUsers().get(friendId).getFriendList().remove(userId);
            } else {
                throw new IncorrectParameterException(String.format("неверно указан %s друга", friendId));
            }
        } else {
            throw new IncorrectParameterException(String.format("неверно указан %s пользователя", userId));
        }
        return true;
    }
}
