package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
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
        return userRepository.getUsers().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    @Override
    public UserDto add(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.getUser(id);
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
        return UserMapper.toUserDto(userRepository.updateUser(updatedUser));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteUser(id);
    }
}
