package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaStorage;

    public MpaServiceImpl(MpaRepository mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    @Override
    public Optional<Mpa> find(Long ratingId) {
        return mpaStorage.find(ratingId);
    }
}