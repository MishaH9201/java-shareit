package ru.practicum.shareit.booking.dto;



import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime  end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
