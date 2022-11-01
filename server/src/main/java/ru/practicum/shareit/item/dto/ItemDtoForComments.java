package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

//import javax.validation.constraints.Digits;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoForComments {
   // @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
  //  @NotNull
   // @NotBlank
    private String name;
  //  @NotNull
    private String description;
  //  @NotNull
    private Boolean available;
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;
    private List<CommentDto> comments;
}
