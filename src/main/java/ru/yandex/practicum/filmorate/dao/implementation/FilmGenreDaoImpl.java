package ru.yandex.practicum.filmorate.dao.implementation;

import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmGenreDao;

@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void deleteAll(Film film) {
        jdbcTemplate.update("DELETE FROM FILMS_GENRES WHERE  FILM_ID = ?", film.getId());
    }

    @Override
    public void updateGenre(Film film) {
        if (film.getGenres() != null) {
            if (film.getGenres().isEmpty()) {
                deleteAll(film);
                return;
            }
            deleteAll(film);
            StringBuilder sb = new StringBuilder("insert into FILMS_GENRES(GENRE_ID, FILM_ID) values ");
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));

            for (Genre genre : film.getGenres())
                sb.append("(").append(genre.getId()).append(",").append(film.getId()).append("),");

            String sql = sb.subSequence(0, sb.length() - 1).toString();
            jdbcTemplate.update(sql);
        }
    }
}
