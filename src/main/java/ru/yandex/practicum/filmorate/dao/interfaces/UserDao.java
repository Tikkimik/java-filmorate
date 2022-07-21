package ru.yandex.practicum.filmorate.dao.interfaces;

import java.util.Map;
import java.util.List;
import ru.yandex.practicum.filmorate.model.user.User;

public interface UserDao {
    List<User> getAllUsers();

    Map<Long, User> getUsers();

    User getUserById(long id);

    List<User> getFriendsList(long id);

    List<User> getListOfMutualFriends(long id, long otherId);

    User addUser(User user);

    User updateUser(User user);

    void deleteFriend(long userId, long friendId);
}
