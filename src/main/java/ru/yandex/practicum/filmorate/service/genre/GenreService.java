package ru.yandex.practicum.filmorate.service.genre;

import java.util.Collection;
import ru.yandex.practicum.filmorate.model.film.Genre;

public interface GenreService {

    Collection<Genre> findAll();

    Genre findById(Integer id);

    void update(Genre genre);
}
