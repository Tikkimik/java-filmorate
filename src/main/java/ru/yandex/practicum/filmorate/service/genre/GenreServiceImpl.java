package ru.yandex.practicum.filmorate.service.genre;

import java.util.Collection;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.dao.interfaces.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Collection<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public Genre findById(Integer id) {
        return genreDao.findById(id);
    }

    @Override
    public void update(Genre genre) {
        genreDao.update(genre);
    }
}
