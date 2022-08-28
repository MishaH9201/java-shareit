package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    User saveUser(User user);

    User updateUser(Long id, User user);

    User getUserById(Long id);

    void deleteUser(Long id);
}