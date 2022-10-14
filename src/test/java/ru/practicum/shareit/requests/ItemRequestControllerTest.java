package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    public final String USER_ID = "X-Sharer-User-Id";
    ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        itemRequestDto = new ItemRequestDto(1l, "Что-то пишушее", LocalDateTime.now(), 1L, Collections.emptyList());
    }

    @Test
    void get() throws Exception {
        when(itemRequestService.getItemRequests(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemRequestService, times(1)).getItemRequests(anyLong());
    }

    @Test
    void getAll() throws Exception {
        when(itemRequestService.getAllItemsRequests(anyLong(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all?from={from}&size={size}", 1, 10)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemRequestService, times(1)).getAllItemsRequests(anyLong(), any());
    }

    @Test
    void getById() throws Exception {
        when(itemRequestService.getItemRequestsById(anyLong(), anyLong())).thenReturn(itemRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", 1L)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
        verify(itemRequestService, times(1)).getItemRequestsById(anyLong(), anyLong());
    }

    @Test
    void add() throws Exception {
        when(itemRequestService.addNewItemRequest(any(), anyLong())).thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header(USER_ID, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
        verify(itemRequestService, times(1)).addNewItemRequest(any(), anyLong());
    }
}