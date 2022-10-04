package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();
    UserDto userDto;
    User user;

    @BeforeEach
    void beforeEach(){
        userDto=new UserDto(1L, "email1@email.com", "Эдуард");
        user=new User(1L, "email1@email.com", "Эдуард");
    }

    @Test
    void getAllUsers() throws Exception{

    }

    @Test
    void getUserById() {
    }

    @Test
    void saveNewUser() throws Exception{
        when(userService.saveUser(any()))
                .thenReturn(user);
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any(), any()))
                .thenReturn(user);
        mockMvc.perform(patch("/users/{userId}", 1,Long.class)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).updateUser(1L,user);
    }

    @Test
    void deleteUser() {
    }
}