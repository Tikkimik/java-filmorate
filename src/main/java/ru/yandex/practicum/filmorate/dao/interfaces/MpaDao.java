package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    Mpa getById(int id);

    List<Mpa> getAll();
}
