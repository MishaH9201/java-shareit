package ru.practicum.shareit.booking.dto;



import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
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
    @FutureOrPresent
    private LocalDateTime  end;
    private Long itemId;
   // private Item item;
    private User booker;

    private BookingStatus status;
}
