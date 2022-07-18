package ru.yandex.practicum.filmorate.service.mpa;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.film.Mpa;

public interface MpaService {

    Optional<Mpa> getById(int id);

    List<Mpa> getAll();

}
