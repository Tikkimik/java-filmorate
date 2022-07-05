package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class User {
    @Min(1) private Long id;
    @Email private String email;
    @NotBlank private String login;
    private String name;
    @Past private LocalDate birthday;
    private Set<Long> friendList = new TreeSet<>(); //тут я не понял почему нельзя сделать сразу трисет <лонг> выдает нулпоинтерексепшн

    public User() {
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
