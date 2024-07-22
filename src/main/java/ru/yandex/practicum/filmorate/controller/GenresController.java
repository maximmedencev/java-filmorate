package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServiceImpl;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenresController {
    private final GenreServiceImpl genreServiceImpl;

    @GetMapping
    public Collection<Genre> findAll() {
        return genreServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public Genre find(@PathVariable Long id) {
        return genreServiceImpl.find(id).orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}
