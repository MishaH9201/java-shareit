package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingDto {
    private Long id;
    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime  end;
    @NotNull
    private Item item;
    @NotNull
    private User booker;
    private BookingStatus status;
}
