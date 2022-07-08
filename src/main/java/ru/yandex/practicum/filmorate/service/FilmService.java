package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmsDao;
import ru.yandex.practicum.filmorate.model.GeneratorId;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {                               //сюда я положил валидацию и айдишники
    private final FilmsDao filmDao;
    private final UsersDao userDao;
    private final GeneratorId generatorId = new GeneratorId();
    private final LocalDate moviesBirthday= LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmsDao filmDao, UsersDao userDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
    }

    public List<Film> getAll() {
        return filmDao.getAll();
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isAfter((moviesBirthday))) {
            film.setId(generatorId.generateId());
            filmDao.addFilm(film);
            return film;
        } else {
            throw new InvalidReleaseDate(String.format("Неверно указана дата релиза фильма: %s.", film));
        }
    }

    public Film updateFilm(Film film) {
        if (filmDao.getFilms().containsKey(film.getId())) {
            filmDao.updateFilm(film);
            return film;
        } else {
            throw new FilmNotFoundException(String.format("Фильм %s для обновления не найден.", film));
        }
    }

    public Film getFilmById(long filmId) {
        if(filmDao.getFilms().containsKey(filmId)) {
            return filmDao.getFilms().get(filmId);
        } else {
            throw new FilmNotFoundException(String.format("Неверно указан идентификатор фильма: %s.", filmId));
        }
    }

    public Film likeTheMovie(long filmId, long userId) {
        if(filmDao.getFilms().containsKey(filmId)) {
            if(userDao.getUsers().containsKey(userId)) {
                filmDao.likeMovie(filmDao.getFilms().get(filmId), userId);
                return filmDao.getFilms().get(filmId);
            } else {
                throw new UserNotFoundException(String.format("Неверно указан идентификатор пользователя: %s.", userId));
            }
        } else {
            throw new FilmNotFoundException(String.format("Неверно указан идентификатор фильма: %s.", filmId));
        }
    }

    public Film removeLikeFromMovie(long filmId, long userId) {
        if(filmDao.getFilms().containsKey(filmId)) {
            if(userDao.getUsers().containsKey(userId)) {
                if(filmDao.getFilms().get(filmId).getNumberOfLikes().contains(userId)) {
                    filmDao.removeLikeFromMovie(filmDao.getFilms().get(filmId), userId);
                    return filmDao.getFilms().get(filmId);
                } else {
                    throw new NotFoundException(String.format("Пользователь с идентификатором: %s не лайкал фильм %b", userId, filmId));
                }
            } else {
                throw new UserNotFoundException(String.format("Неверно указан идентификатор пользователя: %s.", userId));
            }
        } else {
            throw new FilmNotFoundException(String.format("Неверно указан идентификатор фильма: %s.", filmId));
        }
    }

    public List<Film> getMostPopularMovies(int count) {
        List<Film> sortFilms = new ArrayList<>(filmDao.getAll());
        sortFilms.sort((o1, o2) -> Integer.compare(o2.getNumberOfLikes().size(), o1.getNumberOfLikes().size()));
        if(sortFilms.size() < count){
            return sortFilms;
        } else {
            return sortFilms.subList(0, count);
        }
    }
}
