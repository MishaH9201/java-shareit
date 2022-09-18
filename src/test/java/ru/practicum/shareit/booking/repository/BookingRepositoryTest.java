package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Test
    void findByBookerId() {
    }

    @Test
    void findAllByBookerId() {
    }

    @Test
    void findByBookerIdAndStatus() {
    }

    @Test
    void findCorrentBookingsByBookerId() {
    }

    @Test
    void findUpcomingBookingsByBookerId() {
    }

    @Test
    void findPastBookingsByBookerId() {
    }

    @Test
    void findBookingsItemsUser() {
    }

    @Test
    void findUpcomingBookingsItemsUser() {
    }

    @Test
    void findCurrentBookingsItemsUser() {
    }

    @Test
    void findPastBookingsItemsUser() {
    }

    @Test
    void getTopByItem_IdAndBooker_IdOrderByEndAsc() {
    }

    @Test
    void findByItemOwnerIdAndStatusWaiting() {
    }

    @Test
    void findByItemOwnerIdAndStatusRejected() {
    }

    @Test
    void findLastBookingWithItemAndOwner() {
    }

    @Test
    void findNextBookingWithItemAndOwner() {
    }
}
/*
 List<Booking> findByBookerId(Long userId);

    Page<Booking> findAllByBookerId(Long userId, PageRequest pageRequest);

    Page<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, PageRequest pageRequest);

    @Query("select b from Booking b " +
            " where b.booker.id = ?1 and b.end > current_timestamp " +
            "and b.start <= current_timestamp " +
            "order by b.start desc")
    Page<Booking> findCorrentBookingsByBookerId(Long userId,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " where b.booker.id = ?1 and b.end > current_timestamp " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    Page<Booking> findUpcomingBookingsByBookerId(Long userId,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " where b.booker.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    Page<Booking> findPastBookingsByBookerId(Long userId,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 order by b.start desc")
    Page<Booking> findBookingsItemsUser(Long userId,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and b.end > current_timestamp " +
            "order by b.start desc")
    Page<Booking> findUpcomingBookingsItemsUser(Long userId,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and b.end > current_timestamp " +
            "and b.start <= current_timestamp " +
            "order by b.start desc")
    Page<Booking> findCurrentBookingsItemsUser(Long userId,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    Page<Booking> findPastBookingsItemsUser(Long userId,PageRequest pageRequest);

    Optional<Booking> getTopByItem_IdAndBooker_IdOrderByEndAsc(Long itemId, Long userId);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and upper( b.status) = 'WAITING' " +
            "order by b.start desc")
    Page<Booking> findByItemOwnerIdAndStatusWaiting(Long bookerId, BookingStatus status,PageRequest pageRequest);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and upper( b.status) = 'REJECTED' " +
            "order by b.start desc")
    Page<Booking> findByItemOwnerIdAndStatusRejected(Long bookerId, BookingStatus status,PageRequest pageRequest);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 AND upper(b.status) = 'APPROVED'" +
            "AND b.end <= current_timestamp AND b.item.owner.id = ?2 " +
            "ORDER BY b.end DESC ")
    Optional<Booking> findLastBookingWithItemAndOwner(Long itemId, Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 AND upper(b.status) = 'APPROVED'" +
            "AND b.start >= current_timestamp AND b.item.owner.id = ?2 " +
            "ORDER BY b.start ASC ")
    Optional<Booking> findNextBookingWithItemAndOwner(Long itemId, Long ownerId);
}
 */