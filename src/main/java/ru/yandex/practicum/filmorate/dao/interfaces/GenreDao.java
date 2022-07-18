package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreDao {
    Collection<Genre> findAll();

    Optional<Genre> findById(Integer id);

    void deleteAllByFilmId(Long filmId);

    Optional<Genre> create(Genre genre);

    Optional<Genre> update(Genre genre);

}
