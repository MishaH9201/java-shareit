package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable("itemId") long itemId) {
        itemService.getItemById(userId, itemId);
        return ItemMapper.toItemDto(itemService.getItemById(userId, itemId));
    }

    @PostMapping
    public ItemDto add(@Valid @RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto, userId);
        //  itemService.addNewItem(item);
        return ItemMapper.toItemDto(itemService.addNewItem(item));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto, userId);
        item.setId(itemId);
        return ItemMapper.toItemDto(itemService.updateItem(userId, item));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        itemService.search(text);
        return itemService.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}