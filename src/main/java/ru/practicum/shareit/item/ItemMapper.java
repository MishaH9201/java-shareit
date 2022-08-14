package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.dto.ItemDto;

public class ItemMapper {
    public static ItemDto toItemDto(Item item,Long userId) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                userId
               // item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
