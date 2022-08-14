package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
                //userId
               // item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
    public static Item toItem(ItemDto item,Long userId) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                userId,
               //  item.getRequest() != null ? item.getRequest().getId() : null
               // item.getRequest()
                null
        );
    }
}
