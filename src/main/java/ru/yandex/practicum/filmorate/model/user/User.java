package ru.yandex.practicum.filmorate.model.user;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import javax.validation.constraints.*;

@Data
public class User {
    private Long id;
    private String name;
    @Email private String email;
    @NotBlank private String login;
    @Past private LocalDate birthday;
    private Set<Long> friendList = new TreeSet<>();

    public User() {

    }

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.friendList = new TreeSet<>();
        this.name = name.isEmpty() || name.isBlank() ? login : name;
    }

    public User(long id, String email, String login, String name, LocalDate birthday, Set<Long> friendList) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = name.isEmpty() || name.isBlank() ? login : name;
        this.friendList = friendList;
    }

    public User(long id, String email, String login, String name, Date birthday, Collection<Integer> friendList) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday.toLocalDate();
        this.name = name.isEmpty() || name.isBlank() ? login : name;
        this.friendList = convertArrayToSet(friendList);
    }

    public static Set<Long> convertArrayToSet(Collection<Integer> array){
        Set<Long> set = new TreeSet<>();
        for (Integer i : array)
            set.add(Long.valueOf(i));
        return set;
    }
}
