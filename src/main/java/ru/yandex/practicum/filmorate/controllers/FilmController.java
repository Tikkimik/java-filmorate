package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GeneratorId;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private final GeneratorId generatorId = new GeneratorId();
    private final LocalDate moviesBirthday= LocalDate.of(1895, 12, 28);
    //28 декабря 1895 года считается днём рождения кино.

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isAfter(moviesBirthday)) {
            film.setId(generatorId.generateId());
            films.add(film);
            return ResponseEntity.status(HttpStatus.OK).body(film);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(film);
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.set(Math.toIntExact(film.getId()-1), film);
        return film;
    }
}
