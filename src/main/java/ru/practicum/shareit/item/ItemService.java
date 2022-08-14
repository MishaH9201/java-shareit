package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.dto.ItemDto;

import java.util.List;

interface ItemService {
    ItemDto addNewItem( ItemDto item);

    List<ItemDto> getItems(long userId);

    void deleteItem(long userId, long itemId);

    Item updateItem(long userId, ItemDto item);
}
