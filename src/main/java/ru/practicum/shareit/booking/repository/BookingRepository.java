package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long userId);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status);

    @Query("select b from Booking b " +
            " where b.booker.id = ?1 and b.end > current_timestamp " +
            "and b.start <= current_timestamp " +
            "order by b.start desc")
    List<Booking> findCorrentBookingsByBookerId(Long userId);

    @Query("select b from Booking b " +
            " where b.booker.id = ?1 and b.end > current_timestamp " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findUpcomingBookingsByBookerId(Long userId);

    @Query("select b from Booking b " +
            " where b.booker.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findPastBookingsByBookerId(Long userId);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 order by b.start desc")
    List<Booking> findBookingsItemsUser(Long userId);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and b.end > current_timestamp " +
            "order by b.start desc")
    List<Booking> findUpcomingBookingsItemsUser(Long userId);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and b.end > current_timestamp " +
            "and b.start <= current_timestamp " +
            "order by b.start desc")
    List<Booking> findCurrentBookingsItemsUser(Long userId);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findPastBookingsItemsUser(Long userId);

    Optional<Booking> getTopByItem_IdAndBooker_IdOrderByEndAsc(Long itemId, Long userId);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and upper( b.status) = 'WAITING' " +
            "order by b.start desc")
    List<Booking> findByItemOwnerIdAndStatusWaiting(Long bookerId, BookingStatus status);

    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where c.owner.id = ?1 and upper( b.status) = 'REJECTED' " +
            "order by b.start desc")
    List<Booking> findByItemOwnerIdAndStatusRejected(Long bookerId, BookingStatus status);

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

