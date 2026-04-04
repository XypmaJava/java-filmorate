package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получение списка всех пользователей. Всего пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            UserValidator.validate(user);
            user.setId(nextId++);
            users.put(user.getId(), user);
            log.info("Создан новый пользователь: id={}, логин={}", user.getId(), user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            if (!users.containsKey(user.getId())) {
                String errorMsg = "Пользователь с id=" + user.getId() + " не найден";
                log.error(errorMsg);
                throw new ValidationException(errorMsg);
            }
            UserValidator.validate(user);
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: id={}, логин={}", user.getId(), user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            throw e;
        }
    }
}