package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private ItemRequest itemRequest;
    private User user1, user2, user3;
    private Item item1, item2;
    private Booking lastBooking, nextBooking;
    private LocalDateTime date = LocalDateTime.now();

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        user3 = new User(3L, "email3@email.com", "Растислав");
        itemRequest = new ItemRequest(1L, "Что-то пишущее", user2, date);
        item1 = new Item(1L, "Ручка", "Писательный инструмент", true, user1, itemRequest);
        item2 = new Item(2L, "Карандаш", "Писательный инструмент", true, user2, null);
        lastBooking = Booking
                .builder()
                .id(1L)
                .start(date.minusDays(2))
                .end(date.minusDays(2))
                .booker(user3)
                .status(BookingStatus.APPROVED)
                .item(item1)
                .build();
        nextBooking = Booking
                .builder()
                .id(2L)
                .start(date.plusHours(1))
                .end(date.plusHours(2))
                .booker(user3)
                .status(BookingStatus.WAITING)
                .item(item2)
                .build();
    }

    @Test
    void testFindAllBookingsByUserIdWhenIdIsValid() {
        user1 = userService.saveUser(user1);
        user2 = userService.saveUser(user2);
        user3 = userService.saveUser(user3);
        item1 = itemService.addNewItem(ItemMapper.toItemDto(item1), user1.getId());
        item2 = itemService.addNewItem(ItemMapper.toItemDto(item2), user2.getId());
        Booking bookingTest1 = bookingService.save(BookingMapper.toBookingDto(lastBooking), user3.getId());
        Booking bookingTest2 = bookingService.save(BookingMapper.toBookingDto(nextBooking), user3.getId());
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.booker.id = :bookerId", Booking.class);
        List<Booking> bookingsTest = query.setParameter("bookerId", user3.getId()).getResultList();
        assertThat(bookingsTest.size(), equalTo(2));
        assertThat(bookingsTest.get(0).getId(), equalTo(lastBooking.getId()));
        assertThat(bookingsTest.get(1).getId(), equalTo(nextBooking.getId()));
    }
}
