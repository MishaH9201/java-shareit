package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto){
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
              //  .item(bookingDto.getItem())
                .booker(bookingDto.getBooker())
                .status(bookingDto.getStatus())
                .build();
    }
    public static BookingDto toBookingDto(Booking booking){
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }
    public static BookingDtoForUpdate toBookingDtoForUpdate(Booking booking){
        return BookingDtoForUpdate.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }
}
