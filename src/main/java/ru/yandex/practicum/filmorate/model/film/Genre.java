package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Genre {
    @NotBlank private int id;
    @NotBlank private String name;
}