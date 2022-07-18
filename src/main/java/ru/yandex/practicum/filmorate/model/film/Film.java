package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import java.util.Set;
import lombok.Builder;
import lombok.NonNull;
import java.util.List;
import java.util.HashSet;
import java.util.TreeSet;
import java.time.LocalDate;
import javax.validation.constraints.*;

@Data
public class Film {
    private Long id;
    @NotEmpty private String name;
    @Size(max = 200) private String description;
    @PastOrPresent private LocalDate releaseDate;
    @Positive private Integer duration;
    private Set<Long> numberOfLikes = new TreeSet<>();
    private List<Genre> genres;
    private Integer rate;
    @NonNull private Mpa mpa;

    @Builder
    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.numberOfLikes = new HashSet<>();
        this.mpa = mpa;
    }

    public Film() {

    }
}
