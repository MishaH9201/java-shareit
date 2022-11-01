package ru.practicum.shareit.booking.dto;

import lombok.*;

//import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoShort {
    private Long id;
  //  @FutureOrPresent
    private LocalDateTime start;
  //  @FutureOrPresent
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
}


