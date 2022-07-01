package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.NonNull;
import javax.validation.constraints.*;

@lombok.Data
public class Film {
    private Long id;
    @NotEmpty private String name;
    @Size(max = 200) private String description;
    @PastOrPresent private LocalDate releaseDate;
    @Positive private Long duration;
}
