package ru.yandex.practicum.filmorate.dao.implementation;

import java.util.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmDao;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmGenreDao;

@Repository
@Component("FilmDaoImpl")
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDao genreDao;

    private static final String SELECT_ALL = "SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE," +
            "FILMS.DURATION, FILMS.MPAA_ID, MPAA.NAME AS MPAA_NAME FROM films " +
            "JOIN MPAA ON FILMS.MPAA_ID = MPAA.MPAA_ID ORDER BY FILM_ID";

    private static final String SELECT_BY_ID = "SELECT FILMS.FILM_ID AS FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, " +
            "FILMS.RELEASE_DATE, FILMS.DURATION, FILMS.MPAA_ID, MPAA.NAME AS MPAA_NAME, FILMS_GENRES.GENRE_ID AS GID," +
            " GENRES.NAME AS GENRES_NAME, LIKES.USER_ID AS `LIKE` FROM FILMS  LEFT JOIN  FILMS_GENRES ON FILMS.FILM_ID = FILMS_GENRES.FILM_ID " +
            "LEFT JOIN GENRES ON FILMS_GENRES.GENRE_ID = GENRES.GENRE_ID LEFT JOIN LIKES ON FILMS.FILM_ID = LIKES.FILM_ID " +
            "LEFT JOIN MPAA ON FILMS.MPAA_ID = MPAA.MPAA_ID WHERE FILMS.FILM_ID = ?";

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, FilmGenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_ALL);
        if (sqlRowSet.next()) {
            do {
                Film film = new Film(
                        sqlRowSet.getLong("FILM_ID"),
                        sqlRowSet.getString("NAME"),
                        sqlRowSet.getString("DESCRIPTION"),
                        Objects.requireNonNull(sqlRowSet.getDate("RELEASE_DATE")).toLocalDate(),
                        sqlRowSet.getInt("DURATION"),
                        new Mpa(sqlRowSet.getInt("MPAA_ID"), sqlRowSet.getString("MPAA_NAME"))
                );
                films.add(film);
            } while (sqlRowSet.next());
        }
        return films;
    }

    @Override
    public void likeFilm(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> films = new TreeMap<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_ALL);
        if (sqlRowSet.next()) {
            do {
                Film film = new Film(
                        sqlRowSet.getLong("film_id"),
                        sqlRowSet.getString("name"),
                        sqlRowSet.getString("description"),
                        Objects.requireNonNull(sqlRowSet.getDate("release_date")).toLocalDate(),
                        sqlRowSet.getInt("duration"),
                        new Mpa(sqlRowSet.getInt("MPAA_ID"), sqlRowSet.getString("MPAA_NAME"))
                );
                films.put(sqlRowSet.getLong("film_id"), film);
            } while (sqlRowSet.next());
        }
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_BY_ID, id);
        if (sqlRowSet.next()) {
            Set<Genre> genres = new HashSet<>();
            Set<Long> likes = new HashSet<>();

            Film film = Film.builder()
                    .description(sqlRowSet.getString("description"))
                    .id(sqlRowSet.getLong("film_id"))
                    .name(sqlRowSet.getString("NAME"))
                    .releaseDate((Objects.requireNonNull(sqlRowSet.getDate("release_date")).toLocalDate()))
                    .duration(sqlRowSet.getInt("duration"))
                    .build();

            film.setMpa(new Mpa(sqlRowSet.getInt("MPAA_ID"),
                    sqlRowSet.getString("MPAA_NAME")));

            do {
                if (sqlRowSet.getString("GENRES_NAME") != null) {
                    genres.add(new Genre(sqlRowSet.getInt("GID"), sqlRowSet.getString("GENRES_NAME")));
                }
                long l;
                if ((l = sqlRowSet.getLong("LIKE")) != 0L) {
                    likes.add(l);
                }
            } while (sqlRowSet.next());
            film.setGenres(new ArrayList<>(genres));
            film.getNumberOfLikes().addAll(likes);
            getLikesByFilm(film);
            if (film.getGenres().isEmpty())
                film.setGenres(null);
            return film;
        }
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(
            new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("FILMS")
            .usingGeneratedKeyColumns("FILM_ID")
            .executeAndReturnKey(this.filmConvert(film)).longValue()
        );
        genreDao.updateGenre(film);
        likeInjection(film);
        return film;
    }

    public Map<String, Object> filmConvert(Film film) { // как в юзерах неполучилось
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        values.put("MPAA_ID", film.getMpa().getId());
        return values;
    }

    private void getLikesByFilm(Film film) {
        List<Long> likes = jdbcTemplate.query("SELECT * FROM LIKES WHERE FILM_ID = ?", (row, rowNum) ->
            row.getLong("USER_ID"), film.getId());
        film.getNumberOfLikes().addAll(likes);
    }

    private void updateLikes(Film film) {
        if (film.getNumberOfLikes().isEmpty()) {
            deleteLikes(film);
        } else {
            deleteLikes(film);
            likeInjection(film);
        }
    }

    private void likeInjection(Film film) {
        if (film.getNumberOfLikes().isEmpty())
            return;

        try (PreparedStatement pstmt = Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection()
                .prepareStatement("INSERT INTO LIKES (USER_ID, FILM_ID) values (?, ?)"))
        {
            for (Long like : film.getNumberOfLikes()) {
                pstmt.setLong(1, like);
                pstmt.setLong(2, film.getId());
                pstmt.addBatch();
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteLikes(Film film) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE FILM_ID = ?", film.getId());
    }

    @Override
    public void removeLikeFromMovie(long filmId, long userId) {
        String row = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(row, filmId, userId);
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILMS SET NAME = ?, DESCRIPTION = ?,RELEASE_DATE = ?, DURATION = ?, MPAA_ID = ? WHERE FILM_ID = ?",
        film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getId());
        genreDao.updateGenre(film);
        updateLikes(film);
        return film;
    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        String sql = "select FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE,  FILMS.DURATION, FILMS.MPAA_ID, FILMS.NAME AS MPAA_NAME " +
                "from FILMS LEFT JOIN  LIKES on FILMS.FILM_ID  = LIKES.FILM_ID " +
                "GROUP BY FILMS.FILM_ID, LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";

        return jdbcTemplate.query(sql, (sqlRowSet, rowNum) ->
            {
                Film film = new Film(
                        sqlRowSet.getLong("FILM_ID"),
                        sqlRowSet.getString("NAME"),
                        sqlRowSet.getString("DESCRIPTION"),
                        sqlRowSet.getDate("RELEASE_DATE").toLocalDate(),
                        (sqlRowSet.getInt("DURATION")),
                        new Mpa(sqlRowSet.getInt("MPAA_ID"), sqlRowSet.getString("MPAA_NAME")
                        )
                );
                getLikesByFilm(film);
                return film;
            }, count
        );
    }
}
