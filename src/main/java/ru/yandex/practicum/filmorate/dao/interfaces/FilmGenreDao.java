package ru.yandex.practicum.filmorate.dao.interfaces;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;

@Repository
public interface FilmGenreDao {
    void updateGenre(Film film);
}
