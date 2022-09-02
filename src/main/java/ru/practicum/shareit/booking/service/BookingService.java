package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    Booking save(BookingDto bookingDto, Long userId);
}
