package ru.yandex.practicum.filmorate.service.mpa;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.dao.interfaces.MpaDao;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MpaServiceImpl implements MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaServiceImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public Optional<Mpa> getById(int id) {
        return mpaDao.getById(id);
    }

    @Override
    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }
}
