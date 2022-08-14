package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<UserDto> findAll();
    UserDto save(UserDto user);

    UserDto getUserById(Long id);
    UserDto update(Long id,UserDto user);

    void deleteUser(Long id);
}