package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.ConstantsProject;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoForComments> get(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getItems(userId, getPageRequest(from, size));
    }

    @GetMapping("/{itemId}")
    public ItemDtoForComments getById(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                      @PathVariable("itemId") long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                       @RequestBody ItemDto itemDto) {
        Item item = itemService.addNewItem(itemDto, userId);
        return ItemMapper.toItemDto(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(ConstantsProject.USER_ID) long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemDto));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        return itemService.search(text, getPageRequest(from, size));
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                                 @RequestBody CommentDto commentDto,
                                 @PathVariable long itemId) {
        return itemService.createComment(commentDto, userId, itemId);
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("id"));
    }
}