package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User save(User user);

    User getUserById(Long id);
    User update(Long id,User user);

    void deleteUser(Long id);
}