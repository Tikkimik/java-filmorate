package ru.yandex.practicum.filmorate.service.genre;

import java.util.Optional;
import java.util.Collection;
import ru.yandex.practicum.filmorate.model.film.Genre;

public interface GenreService {

    Collection<Genre> findAll();

    Optional<Genre> findById(Integer id);

    Optional<Genre> update(Genre genre);

}
