package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    void testValidFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void testFilmWithEmptyName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void testFilmWithNullName() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void testFilmWithBlankName() {
        Film film = new Film();
        film.setName("   ");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void testFilmWithDescriptionTooLong() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void testFilmWithDescriptionExactly200Chars() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A".repeat(200));
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void testFilmWithReleaseDateBeforeBirthDate() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void testFilmWithReleaseDateAtBirthDate() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(148);

        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void testFilmWithNullReleaseDate() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(null);
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void testFilmWithZeroDuration() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(0);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    void testFilmWithNegativeDuration() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(-10);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    void testFilmWithNullDuration() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    void testFilmWithPositiveDuration() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A great movie");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(1);

        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void testFilmWithNullDescription() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription(null);
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }
}