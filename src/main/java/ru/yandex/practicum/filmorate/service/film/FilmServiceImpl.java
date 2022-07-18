package ru.yandex.practicum.filmorate.service.film;

import java.util.List;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmDao;
import ru.yandex.practicum.filmorate.dao.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.InvalidReleaseDate;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmDao dao;
    private final UserDao userDao;
    private final LocalDate moviesBirthday= LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmServiceImpl(@Qualifier("FilmDaoImpl") FilmDao dao, UserDao userService) {
        this.dao = dao;
        this.userDao = userService;
    }

    @Override
    public List<Film> getAll() {
        return dao.getAll();
    }

    public Film getFilmById(long filmId) {
        if(dao.getFilms().containsKey(filmId)) {
            return dao.getFilmById(filmId);
        } else {
            throw new FilmNotFoundException(String.format("Неверно указан идентификатор фильма: %s.", filmId));
        }
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isAfter((moviesBirthday))) {
            return dao.addFilm(film);
        } else {
            throw new InvalidReleaseDate(String.format("Неверно указана дата релиза фильма: %s.", film));
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (dao.getFilms().containsKey(film.getId())) {
            return dao.updateFilm(film);
        } else {
            throw new FilmNotFoundException(String.format("Фильм %s для обновления не найден.", film));
        }
    }

    public Film likeFilm(long filmId, long userId) {
        if(dao.getFilms().containsKey(filmId)) {
            if(userDao.getUsers().containsKey(userId)) {
                dao.getFilms().get(filmId).getNumberOfLikes().add(userId);
                return dao.getFilms().get(filmId);
            } else {
                throw new UserNotFoundException(String.format("Неверно указан идентификатор пользователя: %s.", userId));
            }
        } else {
            throw new FilmNotFoundException(String.format("Неверно указан идентификатор фильма: %s.", filmId));
        }
    }

    @Override
    public Film removeLikeFromMovie(long filmId, long userId) {
        if(dao.getFilms().containsKey(filmId)) {
            if(userDao.getUsers().containsKey(userId)) {
                if(dao.getFilms().get(filmId).getNumberOfLikes().contains(userId)) {
                    dao.removeLikeFromMovie(dao.getFilms().get(filmId), userId);
                    return dao.getFilms().get(filmId);
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

    @Override
    public List<Film> getMostPopularMovies(int count) {
        return dao.getMostPopular(count);
    }
}