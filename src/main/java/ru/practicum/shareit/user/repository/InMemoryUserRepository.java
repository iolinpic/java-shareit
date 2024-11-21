package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserConflictException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<Long, User>();
    private Long nextId = 0L;

    private void nextIndex() {
        nextId = nextId + 1;
    }

    private void isEmailUnique(String email, Long updatingUserId) {
        User user = users.values().stream().filter(u -> !Objects.equals(u.getId(), updatingUserId)).filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        if (user != null) {
            throw new UserConflictException("Email already exists");
        }
    }

    @Override
    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    @Override
    public User getUser(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public User addUser(User user) {
        isEmailUnique(user.getEmail(), null);
        user.setId(nextId);
        users.put(user.getId(), user);
        nextIndex();
        return user;
    }

    @Override
    public User updateUser(User user) {
        isEmailUnique(user.getEmail(), user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }
}
