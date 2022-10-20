package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoForComments getItemById(Long userId, Long itemId);

    Item addNewItem(ItemDto item, Long userId);

    List<ItemDtoForComments> getItems(long userId, PageRequest pageRequest);

    void deleteItem(long userId, long itemId);

    Item updateItem(long userId, ItemDto item);

    List<ItemDto> search(String text, PageRequest pageRequest);

    CommentDto createComment(CommentDto commentDto, long userId, long itemId);

}
