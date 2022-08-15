package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

interface UserService {
    List<User> getAllUsers();
    User saveUser(User user);

    User updateUser(Long id,User user);

    User getUserById(Long id);

    void deleteUser(Long id);
}