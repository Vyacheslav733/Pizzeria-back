package com.example.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
class UserServiceTests {
    @Autowired
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void createData() {
        removeData();

        user = userService.create(new UserEntity("kruviii", "Павел", "Крылов", "test@test.ru", "11-11-2023", "+79876543210", "qwerty123"));
        userService.create(new UserEntity("vihoof", "Фёдор", "Лопатин", "test1@test.ru", "22-12-2023", "+79875433210", "qwerty321"));
        userService.create(new UserEntity("fragreg", "Семён", "Кукушкин", "test2@test.ru", "03-02-2023", "+74567433210", "qwerty451"));
    }

    @AfterEach
    void removeData() {
        userService.getAll().forEach(item -> userService.delete(item.getId()));
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.get(0L));
    }

    @Transactional
    @Test
    void createTest() {
        Assertions.assertEquals(3, userService.getAll().size());
        Assertions.assertEquals(user, userService.get(user.getId()));
    }

    @Test
    void createNotUniqueTest() {
        final UserEntity nonUniqueUser = new UserEntity("kruviii", "Павел", "Крылов", "test@test.ru", "11-11-2023", "+79876543210", "qwerty123");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.create(nonUniqueUser));
    }

    @Test
    void createNullableTest() {
        final UserEntity nullableUser = new UserEntity(null, "Павел", "Крылов", "test@test.ru", "11-11-2023", "+79876543210", "qwerty123");
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userService.create(nullableUser));
    }

    @Transactional
    @Test
    void updateTest() {
        final String test = "TEST";
        final String oldLogin = user.getLogin();
        final UserEntity newEntity = userService.update(user.getId(),
                new UserEntity("kruvi", "Павел", "Крылов", "test@test.ru", "11-11-2023", "+79876543210", "qwerty123"));
        Assertions.assertEquals(3, userService.getAll().size());
        Assertions.assertEquals(newEntity, userService.get(user.getId()));
        Assertions.assertNotEquals(test, newEntity.getLogin());
        Assertions.assertNotEquals(oldLogin, newEntity.getLogin());
    }

    @Test
    void deleteTest() {
        userService.delete(user.getId());
        Assertions.assertEquals(2, userService.getAll().size());

        final UserEntity newEntity = userService
                .create(new UserEntity("kruviii", "Павел", "Крылов", "test@test.ru", "11-11-2023", "+79876543210", "qwerty123"));
        Assertions.assertEquals(3, userService.getAll().size());
        Assertions.assertNotEquals(user.getId(), newEntity.getId());
    }
}
