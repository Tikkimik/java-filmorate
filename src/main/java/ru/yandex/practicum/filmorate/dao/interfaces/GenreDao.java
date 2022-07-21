package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreDao {
    Collection<Genre> findAll();

    Genre findById(Integer id);

    void deleteAllByFilmId(Long filmId);

    Genre create(Genre genre);

    Genre update(Genre genre);

}
