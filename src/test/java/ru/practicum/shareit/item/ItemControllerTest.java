package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    ItemDto itemDto;
    ItemDtoForComments itemDtoForComments;
    Item item;
    CommentDto comment;
    public final String userId = "X-Sharer-User-Id";

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto(1L, "Ручка", "Писательный инструмент", true, 1L);
        itemDtoForComments = new ItemDtoForComments(1L, "Ручка", "Писательный инструмент", true,
                null, null, Collections.emptyList());
        item = new Item(1L, "Ручка", "Писательный инструмент", true, new User(1L, "a@a.s", "Коля"), null);
        comment = new CommentDto(1L, "Good pen", "Ed", LocalDateTime.now());
    }

    @Test
    void get() throws Exception {
        when(itemService.getItems(anyLong(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(userId, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).getItems(anyLong(), any());
    }

    @Test
    void getById() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDtoForComments);
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                        .header(userId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDtoForComments.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoForComments.getDescription())));
        verify(itemService, times(1)).getItemById(anyLong(), anyLong());
    }

    @Test
    void add() throws Exception {
        when(itemService.addNewItem(any(), any())).thenReturn(item);
        mockMvc.perform(post("/items")
                        .header(userId, 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
        verify(itemService, times(1)).addNewItem(any(), any());
    }

    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/items/{itemId}", 1L)
                        .header(userId, 1L))
                .andExpect(status().isOk());
        verify(itemService, times(1)).deleteItem(1L, 1L);
    }

    @Test
    void update() throws Exception {
        when(itemService.updateItem(anyLong(), any())).thenReturn(item);
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(userId, 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
        verify(itemService, times(1)).updateItem(anyLong(), any());
    }

    @Test
    void search() throws Exception {
        when(itemService.search(any(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/items/search?text={text}&from={from}&size={size}",
                        "text", 1, 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).search(any(), any());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.createComment(any(), anyLong(), anyLong())).thenReturn(comment);
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(userId, 1L)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())));
        verify(itemService, times(1)).createComment(any(), anyLong(), anyLong());
    }
}