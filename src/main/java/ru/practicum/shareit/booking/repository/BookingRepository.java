package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByBookerId(Long userId);
    List<Booking> findByBookerIdAndStatus(Long booker_id, BookingStatus status);

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
}

/*@Query(value = "select it.user_id, count(it.id) as count "+
        "from items as it left join users as us on it.user_id = us.id "+
        "where (cast(us.registration_date as date)) between ?1 and ?2 "+
        "group by it.user_id", nativeQuery = true)
"select b from Booking b left join Item i on b.item.id = i.id where i.owner.id = ?1 order by b.start desc")*/