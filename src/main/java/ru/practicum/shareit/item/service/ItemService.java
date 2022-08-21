package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItemById(Long userId, Long itemId);

    Item addNewItem(Item item);

    List<Item> getItems(long userId);

    void deleteItem(long userId, long itemId);

    Item updateItem(long userId, Item item);

    List<Item> search(String text);
}
