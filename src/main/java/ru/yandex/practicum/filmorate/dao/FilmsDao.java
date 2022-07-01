package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FilmsDao {         //https://www.kelltontech.com/kellton-tech-blog/spring-and-data-access-object-dao-part-1

    private final Map<Long, Film> films = new HashMap<>();

    public Map<Long, Film> getFilms() {
        return films;
    }

    public List<Film> getAll() {
        return films.values().stream().collect(Collectors.toList());
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }
}
