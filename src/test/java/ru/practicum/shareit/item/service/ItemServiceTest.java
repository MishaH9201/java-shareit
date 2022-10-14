package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemServiceTest {
    ItemService itemService;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;
    ItemRequest itemRequest;
    User user1, user2, user3;
    Item item1;
    Booking lastBooking, nextBooking;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        user3 = new User(3L, "email3@email.com", "Растислав");
        itemRequest = new ItemRequest(1L, "Что-то пишущее", user2, LocalDateTime.now());
        item1 = new Item(1L, "Ручка", "Писательный инструмент", true, user1, itemRequest);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);
        lastBooking = Booking
                .builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(2))
                .booker(user3)
                .status(BookingStatus.APPROVED)
                .item(item1)
                .build();
        nextBooking = Booking
                .builder()
                .id(2L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .booker(user3)
                .status(BookingStatus.APPROVED)
                .item(item1)
                .build();
        comment = new Comment(1L, "Отличная ручка", item1, user2, LocalDateTime.now());
    }

    @Test
    void getItemById() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.findLastBookingWithItemAndOwner(Mockito.any(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(lastBooking));
        when(bookingRepository.findNextBookingWithItemAndOwner(Mockito.any(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(nextBooking));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        when(commentRepository.findByItemId(Mockito.anyLong())).thenReturn(Collections.singletonList(comment));
        ItemDtoForComments itemTest = itemService.getItemById(user2.getId(), item1.getId());
        assertEquals("Ручка", itemTest.getName());
        assertEquals("Писательный инструмент", itemTest.getDescription());
        assertEquals(1L, itemTest.getLastBooking().getId());
        assertEquals(2L, itemTest.getNextBooking().getId());
        assertEquals(1, itemTest.getComments().size());
        assertEquals("Отличная ручка", itemTest.getComments().get(0).getText());
        Mockito.verify(itemRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void addNewItem() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.save(Mockito.any())).thenReturn(item1);
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        Item itemTest = itemService.addNewItem(ItemMapper.toItemDto(item1), user2.getId());
        assertEquals("Ручка", itemTest.getName());
        assertEquals("Писательный инструмент", itemTest.getDescription());
        Mockito.verify(itemRepository, times(1)).save(Mockito.any());
    }

    @Test
    void getItems() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(item1)));
        when(bookingRepository.findLastBookingWithItemAndOwner(Mockito.any(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(lastBooking));
        when(bookingRepository.findNextBookingWithItemAndOwner(Mockito.any(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(nextBooking));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        when(commentRepository.findByItemId(Mockito.anyLong())).thenReturn(Collections.singletonList(comment));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        List<ItemDtoForComments> itemsTest = itemService.getItems(user2.getId(), pageRequest);
        assertEquals("Ручка", itemsTest.get(0).getName());
        assertEquals("Писательный инструмент", itemsTest.get(0).getDescription());
        assertEquals(1L, itemsTest.get(0).getLastBooking().getId());
        assertEquals(2L, itemsTest.get(0).getNextBooking().getId());
        assertEquals(1, itemsTest.get(0).getComments().size());
        assertEquals("Отличная ручка", itemsTest.get(0).getComments().get(0).getText());
        Mockito.verify(itemRepository, times(1)).findByOwnerIdOrderById(Mockito.anyLong(), Mockito.any());
    }


    @Test
    void updateItem() {
        Item itemUpdate = new Item(1L, "Карандаш", "Писательный инструмент", true, user1, itemRequest);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item1));
        when(itemRepository.save(Mockito.any())).thenReturn(itemUpdate);
        ItemDto itemDto = new ItemDto(1L, "Карандаш", null, null, 1L);
        Item itemTest = itemService.updateItem(user1.getId(), itemDto);
        assertEquals("Карандаш", itemTest.getName());
        assertEquals("Писательный инструмент", itemTest.getDescription());
        Mockito.verify(itemRepository, times(1)).save(Mockito.any());
    }

    @Test
    void search() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        when(itemRepository.search(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(List.of(item1)));
        List<ItemDto> itemsTest = itemService.search("Руч", pageRequest);
        assertEquals("Ручка", itemsTest.get(0).getName());
        assertEquals("Писательный инструмент", itemsTest.get(0).getDescription());
        Mockito.verify(itemRepository, times(1)).search(Mockito.any(), Mockito.any());
    }

    @Test
    void createComment() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item1));
        when(commentRepository.save(Mockito.any())).thenReturn(comment);
        when(bookingRepository.getTopByItem_IdAndBooker_IdOrderByEndAsc(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(lastBooking));
        CommentDto commentTest = itemService.createComment(CommentMapper.toCommentDto(comment), user3.getId(), item1.getId());
        assertEquals("Отличная ручка", commentTest.getText());
        assertEquals(1L, commentTest.getId());
        Mockito.verify(commentRepository, times(1)).save(Mockito.any());
        Mockito.verify(itemRepository, times(1)).findById(Mockito.anyLong());
    }
}