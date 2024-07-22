package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServiceImpl;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaServiceImpl ratingService;

    @GetMapping
    public Collection<Mpa> findAll() {
        return ratingService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa find(@PathVariable Long id) {
        return ratingService.find(id)
                .orElseThrow(() -> new NotFoundException("Рейтинга с id = " + id + " не существует"));
    }
}
