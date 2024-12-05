package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    @Override
    public UserDto add(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        User updatedUser = UserMapper.toUser(userDto);
        if (updatedUser.getId() == null) {
            updatedUser.setId(user.getId());
        }
        if (updatedUser.getName() == null) {
            updatedUser.setName(user.getName());
        }
        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
