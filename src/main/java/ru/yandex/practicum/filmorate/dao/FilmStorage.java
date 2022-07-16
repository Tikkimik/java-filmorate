package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.stream.Collectors;

public interface FilmStorage {

    List<Film> getAll();                                //вернуть все фильмы

    void addFilm(Film film);                            //добавить фильм

    void updateFilm(Film film);                         //изменить существующий фильм
}
