package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoForComments getItemById(Long userId, Long itemId);

    Item addNewItem(ItemDto item, Long userId);

    List<ItemDtoForComments> getItems(long userId);

    void deleteItem(long userId, long itemId);

    Item updateItem(long userId, ItemDto item);

    List<ItemDto> search(String text);

    CommentDto createComment(CommentDto commentDto, long userId, long itemId);

}
