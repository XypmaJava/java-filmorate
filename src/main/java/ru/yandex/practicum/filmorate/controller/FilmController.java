package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получение списка всех фильмов. Всего фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody Film film) {
        FilmValidator.validate(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: id={}, название={}", film.getId(), film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть null");
        }

        if (film.getId() == null) {
            throw new ValidationException("Id фильма не должен быть null");
        }

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }

        FilmValidator.validate(film);

        films.put(film.getId(), film);
        log.info("Обновлен фильм: id={}, название={}", film.getId(), film.getName());
        return film;
    }
}