package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDtoForComments> get(@RequestHeader(USER_ID) long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoForComments getById(@RequestHeader(USER_ID) long userId,
                           @PathVariable("itemId") long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto add(@Valid @RequestHeader(USER_ID) Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.addNewItem(itemDto, userId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(USER_ID) long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID) Long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        itemService.updateItem(userId, itemDto);
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemDto));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        itemService.search(text);
        return itemService.search(text);
    }
    @PostMapping("{itemId}/comment")
    public CommentDto addComment( @RequestHeader(USER_ID) Long userId,
                                  @RequestBody CommentDto commentDto,
                                 @PathVariable long itemId
    ) {
        return itemService.createComment(commentDto, userId, itemId);
    }
    @GetMapping("/s")
    public List<ItemDto> seaaarch(@RequestParam String text) {
        itemService.search(text);
        return itemService.search(text);
    }

}