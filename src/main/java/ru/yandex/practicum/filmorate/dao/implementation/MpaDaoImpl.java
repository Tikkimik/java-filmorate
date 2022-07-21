package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.dao.interfaces.MpaDao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM MPAA where MPAA_ID = ?", id);
        if (rs.next()) {
            return new Mpa(rs.getInt(1), rs.getString(2));
        }
        return null;
    }

    @Override
    public List<Mpa> getAll() {
        final String sql = "SELECT * FROM MPAA ORDER BY 1 asc ";

        return jdbcTemplate.queryForStream(sql, (rs, rowNum) ->
                        new Mpa(rs.getInt(1), rs.getString(2)))
                .sorted((o1, o2) -> o1.getId() < o2.getId() ? -1 : 1)
                .collect(Collectors.toList());
    }
}
