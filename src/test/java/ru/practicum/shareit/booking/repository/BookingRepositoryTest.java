package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class
BookingRepositoryTest {
@Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    Item item1, item2;
    User user1, user2;

    Booking booking1, booking2;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        userRepository.save(user1);
        userRepository.save(user2);
        item1 = new Item(1L, "Ручка", "Писательный инструмент",true, user1,null);
        item2 = new Item(2L, "Кошка", "Мяукательный инструмент",true, user2,null);
        itemRepository.save(item1);
        itemRepository.save(item2);
        booking1 = new Booking(1L,LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusHours(1),item2, user1, BookingStatus.WAITING);
        booking2 = new Booking(2L,LocalDateTime.now().plusMinutes(2),
                LocalDateTime.now().plusHours(20),item1, user2, BookingStatus.WAITING);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findByBookerId() {
        List<Booking> bookingTest = bookingRepository.findByBookerId(1L);
        assertNotNull(bookingTest);
        assertEquals(1L, bookingTest.get(0).getId());
        assertEquals(booking1.getStart(), bookingTest.get(0).getStart());
        assertEquals(booking1.getEnd(), bookingTest.get(0).getEnd());
    }

    @Test
    void findAllByBookerId() {
        Page<Booking> bookingTest = bookingRepository.findAllByBookerId(1L, PageRequest.of(0,10, Sort.by("start")));
        assertNotNull(bookingTest);
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(1L, bookingTest.getContent().get(0).getId());
        assertEquals(booking1.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking1.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findByBookerIdAndStatus() {
        Page<Booking> bookingTest = bookingRepository.findByBookerIdAndStatus(1L,BookingStatus.WAITING,
                PageRequest.of(0,10, Sort.by("start")));
        assertNotNull(bookingTest);
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(1L, bookingTest.getContent().get(0).getId());
        assertEquals(booking1.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking1.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findCorrentBookingsByBookerId() {
      Booking booking3 = new Booking(3L,LocalDateTime.now(),
                LocalDateTime.now().plusHours(20),item1, user2, BookingStatus.WAITING);
        bookingRepository.save(booking3);
        Page<Booking> bookingTest = bookingRepository.findCorrentBookingsByBookerId(2L,
                PageRequest.of(0,10, Sort.by("start")));
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(3L, bookingTest.getContent().get(0).getId());
        assertEquals(booking3.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking3.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findUpcomingBookingsByBookerId() {
        Page<Booking> bookingTest = bookingRepository.findUpcomingBookingsByBookerId(1L,
                PageRequest.of(0,10, Sort.by("start")));
        assertNotNull(bookingTest);
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(1L, bookingTest.getContent().get(0).getId());
        assertEquals(booking1.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking1.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findPastBookingsByBookerId() {
        Booking booking3 = new Booking(3L,LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusHours(20),item1, user2, BookingStatus.WAITING);
        bookingRepository.save(booking3);
        Page<Booking> bookingTest = bookingRepository. findPastBookingsByBookerId(2L,
                PageRequest.of(0,10, Sort.by("start")));
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(3L, bookingTest.getContent().get(0).getId());
        assertEquals(booking3.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking3.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findBookingsItemsUser() {
        Page<Booking> bookingTest = bookingRepository.findBookingsItemsUser(2L,
                PageRequest.of(0,10, Sort.by("start")));
        assertNotNull(bookingTest);
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(1L, bookingTest.getContent().get(0).getId());
        assertEquals(booking1.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking1.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findUpcomingBookingsItemsUser() {
        Page<Booking> bookingTest = bookingRepository.findUpcomingBookingsItemsUser(2L,
                PageRequest.of(0,10, Sort.by("start")));
        assertNotNull(bookingTest);
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(1L, bookingTest.getContent().get(0).getId());
        assertEquals(booking1.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking1.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findCurrentBookingsItemsUser() {
        Booking booking3 = new Booking(3L,LocalDateTime.now(),
                LocalDateTime.now().plusHours(20),item1, user2, BookingStatus.WAITING);
        bookingRepository.save(booking3);
        Page<Booking> bookingTest = bookingRepository.findCurrentBookingsItemsUser(1L,
                PageRequest.of(0,10, Sort.by("start")));
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(3L, bookingTest.getContent().get(0).getId());
        assertEquals(booking3.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking3.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findPastBookingsItemsUser() {
        Booking booking3 = new Booking(3L,LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusHours(20),item1, user2, BookingStatus.WAITING);
        bookingRepository.save(booking3);
        Page<Booking> bookingTest = bookingRepository. findPastBookingsItemsUser(1L,
                PageRequest.of(0,10, Sort.by("start")));
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(3L, bookingTest.getContent().get(0).getId());
        assertEquals(booking3.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking3.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void getTopByItem_IdAndBooker_IdOrderByEndAsc() {
        Optional<Booking> bookingTest = bookingRepository.getTopByItem_IdAndBooker_IdOrderByEndAsc(1L,2L);
        assertNotNull(bookingTest);
        assertEquals(2L, bookingTest.get().getId());
        assertEquals(booking2.getStart(), bookingTest.get().getStart());
        assertEquals(booking2.getEnd(), bookingTest.get().getEnd());
    }

    @Test
    void findByItemOwnerIdAndStatusWaiting() {
        Page<Booking> bookingTest = bookingRepository.findByItemOwnerIdAndStatusWaiting(1L,BookingStatus.WAITING,
                PageRequest.of(0,10, Sort.by("start")));
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(2L, bookingTest.getContent().get(0).getId());
        assertEquals(booking2.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking2.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findByItemOwnerIdAndStatusRejected() {
        booking2.setStatus(BookingStatus.REJECTED);
        Page<Booking> bookingTest = bookingRepository.findByItemOwnerIdAndStatusWaiting(1L,BookingStatus.REJECTED,
                PageRequest.of(0,10, Sort.by("start")));
        assertEquals(1,bookingTest.getContent().size());
        assertEquals(2L, bookingTest.getContent().get(0).getId());
        assertEquals(booking2.getStart(), bookingTest.getContent().get(0).getStart());
        assertEquals(booking2.getEnd(), bookingTest.getContent().get(0).getEnd());
    }

    @Test
    void findLastBookingWithItemAndOwner() {
        Booking booking3 = new Booking(3L,LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),item1, user2, BookingStatus.APPROVED);
        bookingRepository.save(booking3);
       Booking d = bookingRepository.findById(3L).orElse(null);
        Optional<Booking> bookingTest = bookingRepository.findLastBookingWithItemAndOwner(1L,1L);
        assertNotNull(bookingTest);
        assertEquals(3L, bookingTest.get().getId());
        assertEquals(booking3.getStart(), bookingTest.get().getStart());
        assertEquals(booking3.getEnd(), bookingTest.get().getEnd());
    }

    @Test
    void findNextBookingWithItemAndOwner() {
        Booking booking3 = new Booking(3L,LocalDateTime.now().plusSeconds(2),
                LocalDateTime.now().plusHours(1),item1, user2, BookingStatus.APPROVED);
        bookingRepository.save(booking3);
        Booking d = bookingRepository.findById(3L).orElse(null);
        Optional<Booking> bookingTest = bookingRepository.findNextBookingWithItemAndOwner(1L,1L);
        assertNotNull(bookingTest);
        assertEquals(3L, bookingTest.get().getId());
        assertEquals(booking3.getStart(), bookingTest.get().getStart());
        assertEquals(booking3.getEnd(), bookingTest.get().getEnd());
    }
}
