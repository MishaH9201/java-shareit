package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path ="/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @PostMapping
    public ItemDto add(@Valid @RequestHeader("X-Sharer-User-Id") Long userId,
                    @Valid @RequestBody Item item) {
        ItemDto itemDto=ItemMapper.toItemDto(item,userId);
        return itemService.addNewItem(itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
    @PatchMapping
    public Item update(@Valid @RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody Item item) {
        ItemDto itemDto=ItemMapper.toItemDto(item,userId);
        return itemService.updateItem(userId, itemDto);
    }
}