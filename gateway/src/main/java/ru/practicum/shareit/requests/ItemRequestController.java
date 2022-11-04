package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.util.ConstantsProject;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(ConstantsProject.USER_ID) long userId) {
        return itemRequestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                         @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("Get requests with , userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllItemsRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                          @PathVariable("requestId") long requestId) {
        log.info("Get request with, requestId={} userId={}", requestId, userId);
        return itemRequestClient.getItemRequestsById(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                                      @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Creating request {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.addNewItemRequest(itemRequestDto, userId);
    }
}



