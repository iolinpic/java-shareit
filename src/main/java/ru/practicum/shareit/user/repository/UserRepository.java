package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
//    List<User> getUsers();
//
//    User getUser(Long id);
//
//    User addUser(User user);
//
//    User updateUser(User user);
//
//    void deleteUser(Long id);
}
