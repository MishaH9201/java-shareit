package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Get all users");
        return repository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User saveUser(User user) {
        log.info("Save new user");
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
       // log.info("Update user");
       User userUpdate = repository.findById(id)
               .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getEmail() != null) {
           // checkRepeatEmail(user);
            userUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userUpdate.setName(user.getName());
        }
       // users.put(id, userUpdate);
        return userUpdate;
    }

    @Override
    public User getUserById(Long id) {
        log.info("Get user");
        return repository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user");
        repository.deleteById(id);
    }
   /* private void checkRepeatEmail(User user) {
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No email");
        }
        if (users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(user.getEmail()::equals)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
    }*/
}