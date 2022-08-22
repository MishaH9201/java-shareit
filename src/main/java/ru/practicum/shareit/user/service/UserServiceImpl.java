package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        log.info("Get all users");
        return repository.findAll();
    }

    @Override
    public User saveUser(User user) {
        log.info("Save new user");
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Update user");
        return repository.update(id, user);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Get user");
        return repository.getUserById(id);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user");
        repository.deleteUser(id);
    }
}