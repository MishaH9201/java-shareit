package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long userId);

    List<Booking> findByBookerIdAndStatus(Long booker_id, BookingStatus status);

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
    @Query("select b from Booking b " +
            " left join Item c on b.item.id = c.id " +
            " where b.booker.id = ?1 and b.end > current_timestamp " +
            "and c.id = ?1"+
            "and upper( b.status) = 'APPROVED'" +
            "order by b.start desc")
Booking findBookingForCheck(Long bookerId,Long itemId);
}

