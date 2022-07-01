package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

@Data
public class User {
    private Long id;
    @Email private String email;
    @NotBlank private String login;
    private String name;
    @Past private LocalDate birthday;

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

    public User(String s, String login_login, LocalDate of) {
    }
}
