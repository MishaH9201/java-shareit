package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime  end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
