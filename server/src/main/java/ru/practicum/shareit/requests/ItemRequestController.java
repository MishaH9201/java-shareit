package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.util.ConstantsProject;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDto> get(@RequestHeader(ConstantsProject.USER_ID) long userId) {

        return itemRequestService.getItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get requests with , userId={}, from={}, size={}",  userId, from, size);
        return itemRequestService.getAllItemsRequests(userId, getPageRequest(from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                  @PathVariable("requestId") long requestId) {
        log.info("Get request with, requestId={} userId={}",requestId,  userId);
        return itemRequestService.getItemRequestsById(userId, requestId);
    }


    @PostMapping
    public ItemRequestDto add(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addNewItemRequest(itemRequestDto, userId);
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("created"));
    }

}



