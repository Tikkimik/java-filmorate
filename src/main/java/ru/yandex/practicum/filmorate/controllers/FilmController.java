package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final  FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAll();
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public Film createNewFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    @ResponseBody
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeTheMovie(@PathVariable long id, @PathVariable long userId) {
        return filmService.likeTheMovie(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeFromMovie(@PathVariable long id, @PathVariable long userId) {
        return filmService.removeLikeFromMovie(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularMovies(@RequestParam(defaultValue = "10") int count) {
        return filmService.getMostPopularMovies(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }


}
