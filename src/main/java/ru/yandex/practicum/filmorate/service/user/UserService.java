package ru.yandex.practicum.filmorate.service.user;

import java.util.List;
import ru.yandex.practicum.filmorate.model.user.User;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    List<User> getFriendsList(long id);

    List<User> getListOfMutualFriends(long id, long otherId);

    User addUser(User user);

    boolean addToFriendList(long id, long friendId);

    User updateUser(User user);

    void removeFromFriendList(long id, long userId);
}
