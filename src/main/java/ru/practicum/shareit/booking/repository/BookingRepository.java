package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
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

