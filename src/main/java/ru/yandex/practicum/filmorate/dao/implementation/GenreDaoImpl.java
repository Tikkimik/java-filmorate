package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.dao.interfaces.GenreDao;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> findAll() {
        return jdbcTemplate.queryForStream("SELECT * FROM GENRES",
                (sqlRowSet, rowNum) -> new Genre(sqlRowSet.getInt("GENRE_ID"),
                        sqlRowSet.getString("NAME"))).collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE genre_id =  ?", id);
        if (rs.next()) {
            return Optional.of(new Genre(rs.getInt(1),
                    rs.getString(2)));
        }
        return Optional.empty();
    }

    @Override
    public void deleteAllByFilmId(Long filmId) {
        final String sql = "DELETE FROM FILMS_GENRES where GENRE_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public Optional<Genre> create(Genre genre) {
        if (jdbcTemplate.update("INSERT INTO GENRES (NAME) VALUES ( ? )", genre.getName()) != 1) {
            return Optional.empty();
        } else {
            return Optional.of(genre);
        }
    }

    @Override
    public Optional<Genre> update(Genre genre) {
        if (jdbcTemplate.update("UPDATE GENRES SET NAME = ? WHERE genre_id = ?", (genre.getId())) != 1) {
            return Optional.empty();
        } else {
            return Optional.of(genre);
        }
    }
}
