package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.util.ConstantsProject;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


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
                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemsRequests(userId, getPageRequest(from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                  @PathVariable("requestId") long requestId) {
        return itemRequestService.getItemRequestsById(userId, requestId);
    }


    @PostMapping
    public ItemRequestDto add(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                              @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addNewItemRequest(itemRequestDto, userId);
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.by("created"));
    }

}



