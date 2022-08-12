package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService {
    Item addNewItem(long userId, Item item);

    List<Item> getItems(long userId);

    void deleteItem(long userId, long itemId);

    Item updateItem(long userId, Item item);
}
