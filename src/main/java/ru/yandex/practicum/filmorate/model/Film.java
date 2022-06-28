package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.NonNull;
import javax.validation.constraints.*;

@lombok.Data
public class Film {
    private Long id;
    @NonNull @NotEmpty private String name;
    @NonNull @Size(max = 200) private String description;
    @NonNull @PastOrPresent private LocalDate releaseDate;
    @NonNull @Positive private Long duration;
}
