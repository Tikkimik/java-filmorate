package ru.yandex.practicum.filmorate.service.film;

import java.util.List;
import ru.yandex.practicum.filmorate.model.film.Film;

public interface FilmService {

    List<Film> getAll();

    Film getFilmById(long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film likeFilm(long filmId, long userId);

    Film removeLikeFromMovie(long filmId, long userId);

    List<Film> getMostPopularMovies(int count);
}
