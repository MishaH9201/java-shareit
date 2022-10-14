package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {
    BookingRepository bookingRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingService bookingService;

    User user1, user2, user3;
    Item item1;

    Booking lastBooking, nextBooking;
    PageRequest pageRequest;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        user3 = new User(3L, "email3@email.com", "Растислав");
        item1 = new Item(1l, "Ручка", "Писательный инструмент", true, user1, null);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
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
                .booker(user2)
                .status(BookingStatus.WAITING)
                .item(item1)
                .build();
        pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
    }

    @Test
    void save() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.save(Mockito.any())).thenReturn(nextBooking);
        Booking bookingTest = bookingService.save(BookingMapper.toBookingDto(nextBooking), user2.getId());
        assertEquals(2L, bookingTest.getId());
        assertEquals(2L, bookingTest.getBooker().getId());
        assertEquals(1L, bookingTest.getItem().getId());
        Mockito.verify(bookingRepository, times(1)).save(Mockito.any());

    }

    @Test
    void shouldUpdateIfApprovedTrue() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.save(Mockito.any())).thenReturn(nextBooking);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        Booking bookingTest = bookingService.update(user1.getId(), nextBooking.getId(), true);
        assertEquals(2L, bookingTest.getId());
        assertEquals(2L, bookingTest.getBooker().getId());
        assertEquals(1L, bookingTest.getItem().getId());
        assertEquals(BookingStatus.APPROVED, bookingTest.getStatus());
        Mockito.verify(bookingRepository, times(1)).save(Mockito.any());
        Mockito.verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void shouldUpdateIfApprovedFalse() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.save(Mockito.any())).thenReturn(nextBooking);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        Booking bookingTest = bookingService.update(user1.getId(), nextBooking.getId(), false);
        assertEquals(2L, bookingTest.getId());
        assertEquals(2L, bookingTest.getBooker().getId());
        assertEquals(1L, bookingTest.getItem().getId());
        assertEquals(BookingStatus.REJECTED, bookingTest.getStatus());
        Mockito.verify(bookingRepository, times(1)).save(Mockito.any());
        Mockito.verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void findBookingById() {
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        BookingDtoForUpdate bookingTest = bookingService.findBookingById(nextBooking.getId(), user2.getId());
        assertEquals(2L, bookingTest.getId());
        assertEquals(2L, bookingTest.getBooker().getId());
        assertEquals(1L, bookingTest.getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void shouldFindAllBookingsByUserIdIfStateAll() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsByUserId(user2.getId(), "ALL", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findAllByBookerId(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsByUserIdIfStateCurrent() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findCorrentBookingsByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsByUserId(user2.getId(), "CURRENT", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findCorrentBookingsByBookerId(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsByUserIdIfStatePast() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(lastBooking));
        when(bookingRepository.findPastBookingsByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsByUserId(user2.getId(), "PAST", pageRequest);
        assertEquals(1L, bookingsTest.get(0).getId());
        assertEquals(3L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findPastBookingsByBookerId(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsByUserIdIfStateFuture() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findUpcomingBookingsByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsByUserId(user2.getId(), "FUTURE", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findUpcomingBookingsByBookerId(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsByUserIdIfStateWaiting() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsByUserId(user2.getId(), "WAITING", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsByUserIdIfStateRejected() {
        nextBooking.setStatus(BookingStatus.REJECTED);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsByUserId(user2.getId(), "REJECTED", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsForItemsUserIfStateAll() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findBookingsItemsUser(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsForItemsUser(user2.getId(), "ALL", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findBookingsItemsUser(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsForItemsUserIfStateCurrent() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findCurrentBookingsItemsUser(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsForItemsUser(user2.getId(), "CURRENT", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findCurrentBookingsItemsUser(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsForItemsUserIfStatePast() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(lastBooking));
        when(bookingRepository.findPastBookingsItemsUser(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(lastBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsForItemsUser(user2.getId(), "PAST", pageRequest);
        assertEquals(1L, bookingsTest.get(0).getId());
        assertEquals(3L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findPastBookingsItemsUser(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsForItemsUserIfStateFuture() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findUpcomingBookingsItemsUser(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsForItemsUser(user2.getId(), "FUTURE", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findUpcomingBookingsItemsUser(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsForItemsUserIfStateWaiting() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findByItemOwnerIdAndStatusWaiting(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsForItemsUser(user2.getId(), "WAITING", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findByItemOwnerIdAndStatusWaiting(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldFindAllBookingsForItemsUserIfStateRejected() {
        nextBooking.setStatus(BookingStatus.REJECTED);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(nextBooking));
        when(bookingRepository.findByItemOwnerIdAndStatusRejected(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(nextBooking)));
        List<BookingDtoForUpdate> bookingsTest = bookingService.findAllBookingsForItemsUser(user2.getId(), "REJECTED", pageRequest);
        assertEquals(2L, bookingsTest.get(0).getId());
        assertEquals(2L, bookingsTest.get(0).getBooker().getId());
        assertEquals(1L, bookingsTest.get(0).getItem().getId());
        Mockito.verify(bookingRepository, times(1)).findByItemOwnerIdAndStatusRejected(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }
}