package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.dto.ItemDto;

import java.util.List;

interface ItemRepository {

    List<ItemDto> findByUserId(long userId);

    ItemDto save(ItemDto item);

    void deleteByUserIdAndItemId(long userId, long itemId);

    Item updateItem(long userId, ItemDto item);
}