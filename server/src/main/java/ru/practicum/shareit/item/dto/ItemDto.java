package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//import javax.validation.constraints.Digits;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
   // @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
//    @NotNull
//    @NotBlank
    private String name;
   // @NotNull
    private String description;
 //   @NotNull
    private Boolean available;
    private Long requestId;
}
