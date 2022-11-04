package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.util.ConstantsProject;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get items");
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                          @PathVariable("itemId") long itemId) {
        log.info("Creating item {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestHeader(ConstantsProject.USER_ID) Long userId,
                                      @Valid @RequestBody ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.addNewItem(itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                             @PathVariable long itemId) {
        log.info("Delete itemId={}, userId={}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                                         @PathVariable long itemId,
                                         @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        log.info("Updating itemId={}, userId={}", itemId, userId);
        return itemClient.updateItem(userId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get items with text {}, from={}, size={}", text, from, size);
        return itemClient.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @PathVariable long itemId) {
        log.info("Creating comment commentDto {}, userId={}, itemId={}", commentDto, userId, itemId);
        return itemClient.createComment(commentDto, userId, itemId);
    }
}