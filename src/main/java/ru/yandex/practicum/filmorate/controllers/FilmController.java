package ru.yandex.practicum.filmorate.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return service.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularMovies(@RequestParam(defaultValue = "10") int count) {
        return service.getMostPopularMovies(count);
    }

    @PostMapping
    public Film createNewFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable long id, @PathVariable long userId) {
        return service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeFromMovie(@PathVariable long id, @PathVariable long userId) {
        return service.removeLikeFromMovie(id, userId);
    }
}
