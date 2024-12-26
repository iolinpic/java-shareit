package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final EntityManager entityManager;
    private final UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "john", "j@example.ru"));
    }

    @Test
    void getAll() {
        List<UserDto> users = userService.getAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(userDto.getId(), users.getFirst().getId());
    }

    @Test
    void getById() {
        UserDto user = userService.getById(userDto.getId());

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
    }

    @Test
    void add() {
        UserDto userLocal = new UserDto(null, "john", "j1@example.ru");

        userLocal = userService.add(userLocal);

        TypedQuery<User> query = entityManager.createQuery("SELECT u from User as u where u.email = :email", User.class);
        User result = query.setParameter("email", userLocal.getEmail()).getSingleResult();

        assertNotNull(result);
        assertEquals(result.getName(), userLocal.getName());
        assertEquals(result.getEmail(), userLocal.getEmail());

    }

    @Test
    void update() {
        UserDto updatedUser = new UserDto(userDto.getId(), "john", null);

        updatedUser = userService.update(userDto.getId(), updatedUser);

        TypedQuery<User> query = entityManager.createQuery("SELECT u from User as u where u.id = :id", User.class);
        User result = query.setParameter("id", updatedUser.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(result.getName(), updatedUser.getName());
        assertEquals(result.getEmail(), userDto.getEmail());

        updatedUser.setName(null);
        updatedUser.setEmail("john2@example.ru");

        updatedUser = userService.update(userDto.getId(), updatedUser);

        result = query.setParameter("id", updatedUser.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getEmail(), updatedUser.getEmail());
    }

    @Test
    void delete() {
        userService.delete(userDto.getId());

        TypedQuery<User> query = entityManager.createQuery("SELECT u from User as u where u.email = :email", User.class);

        assertThrows(NoResultException.class, () -> query.setParameter("email", userDto.getEmail()).getSingleResult());
    }
}