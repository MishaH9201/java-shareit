package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();
    UserDto saveUser(UserDto user);

    UserDto updateUser(Long id,UserDto user);

    UserDto getUserById(Long id);

    void deleteUser(Long id);
}