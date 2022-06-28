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
}
