package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private User user1;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user1));
        List<UserDto> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Эдуард", users.get(0).getName());
        assertEquals("email1@email.com", users.get(0).getEmail());
        Mockito.verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(Mockito.any())).thenReturn(user1);
        User userTest = userService.saveUser(user1);
        assertEquals("Эдуард", userTest.getName());
        assertEquals("email1@email.com", userTest.getEmail());
        Mockito.verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    void testUpdateUser() {
        User userUpdate = new User(1L, "email1@email.com", "Николай");
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(Mockito.any())).thenReturn(userUpdate);
        userService.saveUser(user1);
        User userTest = userService.updateUser(1L, userUpdate);
        assertEquals("Николай", userTest.getName());
        assertEquals("email1@email.com", userTest.getEmail());
        Mockito.verify(userRepository, times(1)).findById(Mockito.any());
        Mockito.verify(userRepository, times(2)).save(Mockito.any());
    }

    @Test
    void getUserByIdWhenIdIsValid() {
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(user1));
        User userTest = userService.getUserById(1L);
        assertEquals("Эдуард", userTest.getName());
        assertEquals("email1@email.com", userTest.getEmail());
        Mockito.verify(userRepository, times(1)).findById(Mockito.any());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(anyLong());
        Mockito.verify(userRepository, times(1)).deleteById(anyLong());
    }
}