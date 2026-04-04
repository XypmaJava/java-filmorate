package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Film validFilm;

    @BeforeEach
    void setUp() {
        validFilm = new Film();
        validFilm.setName("Inception");
        validFilm.setDescription("A great movie about dreams");
        validFilm.setReleaseDate(LocalDate.of(2010, 7, 16));
        validFilm.setDuration(148);
    }

    @Test
    void testAddFilmSuccess() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Inception")))
                .andExpect(jsonPath("$.duration", is(148)));
    }

    @Test
    void testAddFilmWithEmptyName() throws Exception {
        validFilm.setName("");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Название фильма не может быть пустым")));
    }

    @Test
    void testAddFilmWithLongDescription() throws Exception {
        validFilm.setDescription("A".repeat(201));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Максимальная длина описания")));
    }

    @Test
    void testAddFilmWithOldReleaseDate() throws Exception {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("не раньше 28 декабря 1895 года")));
    }

    @Test
    void testAddFilmWithNegativeDuration() throws Exception {
        validFilm.setDuration(-10);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Продолжительность фильма должна быть положительным числом")));
    }

    @Test
    void testGetAllFilms() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testUpdateFilm() throws Exception {
        // Add a film first
        var response = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk())
                .andReturn();

        Film addedFilm = objectMapper.readValue(response.getResponse().getContentAsString(), Film.class);

        // Update the film
        addedFilm.setName("Inception 2");
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addedFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Inception 2")));
    }

    @Test
    void testUpdateNonExistentFilm() throws Exception {
        validFilm.setId(999);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("не найден")));
    }
}