package ru.yandex.practicum.filmorate.dao.interfaces;

import java.util.Map;
import java.util.List;
import ru.yandex.practicum.filmorate.model.film.Film;

public interface FilmDao {

    List<Film> getAll();

    Map<Long, Film> getFilms();

    Film getFilmById(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getMostPopular(Integer count);

    void likeFilm(long filmId, long userId);

    void removeLikeFromMovie(long filmId, long userId);
}
