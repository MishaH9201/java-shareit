package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;

import java.util.List;

public interface BookingService {
    Booking save(BookingDto bookingDto, Long userId);

    Booking update(Long userId, long bookingId, boolean approved);

    BookingDtoForUpdate findBookingById(long bookingId, long userId);

    List<BookingDtoForUpdate> findAllBookingsByUserId(Long userId, String state);

    List<BookingDtoForUpdate> findAllBookingsForItemsUser(Long userId, String state);
}
