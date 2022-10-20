package ru.practicum.shareit.requests.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;
    @NotBlank
    @NotNull
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private Long requestor;
    private List<ItemDto> items;


}
