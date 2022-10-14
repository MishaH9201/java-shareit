package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemRequestServiceImplTest {
    ItemRequestService itemRequestService;
    UserRepository userRepository;
    ItemRepository itemRepository;
    ItemRequestRepository itemRequestRepository;
    ItemRequest itemRequest;
    User user1, user2;
    Item item1;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        itemRequest = new ItemRequest(1L, "Что-то пишущее", user2, LocalDateTime.now());
        item1 = new Item(1l, "Ручка", "Писательный инструмент", true, user1, null);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository,userRepository,itemRepository);
    }

    @Test
    void addNewItemRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRequestRepository.save(Mockito.any())).thenReturn(itemRequest);
        ItemDto itemDto= ItemMapper.toItemDto(item1);
        ItemRequestDto itemRequestTest = itemRequestService.addNewItemRequest
                (ItemRequestMapper.toItemRequestDto(itemRequest, Collections.singletonList(itemDto)), user2.getId());
        assertEquals(1L, itemRequestTest.getId());
        assertEquals("Что-то пишущее", itemRequestTest.getDescription());
        Mockito.verify(itemRequestRepository, times(1)).save(Mockito.any());
    }

    @Test
    void getItemRequests() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(Collections.singletonList(item1));
        when(itemRequestRepository.findByRequestorId(Mockito.any())).thenReturn(Collections.singletonList(itemRequest));
        List<ItemRequestDto> itemRequestsTest= itemRequestService.getItemRequests(user2.getId());
        assertEquals(1L, itemRequestsTest.get(0).getId());
        assertEquals("Что-то пишущее", itemRequestsTest.get(0).getDescription());
        Mockito.verify(itemRequestRepository, times(1)).findByRequestorId(Mockito.any());
    }

    @Test
    void getItemRequestsById() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(Collections.singletonList(item1));
        when(itemRequestRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(itemRequest));
        ItemRequestDto itemRequestTest= itemRequestService.getItemRequestsById(user2.getId(), itemRequest.getId());
        assertEquals(1L, itemRequestTest.getId());
        assertEquals("Что-то пишущее", itemRequestTest.getDescription());
        Mockito.verify(itemRequestRepository, times(1)).findById(Mockito.any());
    }

    @Test
    void getAllItemsRequests() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(Collections.singletonList(item1));
        when(itemRequestRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(itemRequest)));
        List<ItemRequestDto> itemRequestsTest= itemRequestService.getAllItemsRequests(user1.getId(),pageRequest);
        assertEquals(1L, itemRequestsTest.get(0).getId());
        assertEquals("Что-то пишущее", itemRequestsTest.get(0).getDescription());
        Mockito.verify(itemRequestRepository, times(1)).findAll(pageRequest);
    }
}