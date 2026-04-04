package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public Film addFilm(@RequestBody Film film) {
        try {
            FilmValidator.validate(film);
            film.setId(nextId++);
            films.put(film.getId(), film);
            log.info("Доба��лен новый фильм: id={}, название={}", film.getId(), film.getName());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            if (!films.containsKey(film.getId())) {
                String errorMsg = "Фильм с id=" + film.getId() + " не найден";
                log.error(errorMsg);
                throw new ValidationException(errorMsg);
            }
            FilmValidator.validate(film);
            films.put(film.getId(), film);
            log.info("Обновлен фильм: id={}, название={}", film.getId(), film.getName());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            throw e;
        }
    }
}