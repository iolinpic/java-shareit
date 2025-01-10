package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.UserDto;

@RequiredArgsConstructor
@RestController()
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;


    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("Get user by id: {}", id);
        return userClient.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Validated({Create.class, Default.class}) @RequestBody UserDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Updating user {} with {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Deleting user {}", id);
        return userClient.deleteUser(id);
    }

}
