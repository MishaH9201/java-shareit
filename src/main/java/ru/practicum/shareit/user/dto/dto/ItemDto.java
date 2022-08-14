package ru.practicum.shareit.user.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long userId;
    private ItemRequest request;
    public ItemDto(Long id, String name, String description, Boolean available, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.userId = userId;
    }

    //  private User owner;

}
