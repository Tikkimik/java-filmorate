package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import javax.validation.constraints.*;

@lombok.Data
public class Film {
    private Long id;
    @NotEmpty private String name;
    @Size(max = 200) private String description;
    @PastOrPresent private LocalDate releaseDate;
    @Positive private Long duration;
    private Set<Long> numberOfLikes = new TreeSet<>();

    public Film() {
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
