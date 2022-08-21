package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> findByUserId(long userId);

    List<Item> getAll();

    Item save(Item item);

    void deleteByUserIdAndItemId(long userId, long itemId);

    Item updateItem(long userId, Item item);

    Item getItemById(Long userId, Long itemId);
}