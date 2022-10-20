package ru.practicum.shareit.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1L, "email1@email.com", "Эдуард");
        user = new User(1L, "email1@email.com", "Эдуард");
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).getAllUsers();

    }

    @Test
    void getUserByIdWhenIdIsValid() throws Exception {
        when(userService.getUserById(any())).thenReturn(user);
        mockMvc.perform(get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testSaveNewUser() throws Exception {
        when(userService.saveUser(any())).thenReturn(user);
        getMockPost()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(2)).saveUser(any());
    }


    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(any(), any()))
                .thenReturn(user);
        getMockPatch()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).updateUser(any(), any());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUser(1L);
    }

    private ResultActions getMockPost() throws Exception {
        return mockMvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions getMockPatch() throws Exception {
        return mockMvc.perform(patch("/users/{userId}", 1L)
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
}