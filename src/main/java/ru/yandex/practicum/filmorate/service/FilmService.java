package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmsDao;
import ru.yandex.practicum.filmorate.model.GeneratorId;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {                               //сюда я положил валидацию и айдишники

    private final GeneratorId generatorId = new GeneratorId();
    private final LocalDate moviesBirthday= LocalDate.of(1895, 12, 28);

    @Autowired
    private FilmsDao filmDao;

    public List<Film> getAll() {
        return filmDao.getAll();
    }

    public ResponseEntity<Film> addFilm(Film film) {
        if (film.getReleaseDate().isAfter((moviesBirthday))) {
            film.setId(generatorId.generateId());
            filmDao.addFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Film> updateFilm(Film film) {
        if (filmDao.getFilms().containsKey(film.getId())) {
            filmDao.updateFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
