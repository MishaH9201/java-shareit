package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
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
                                       @PositiveOrZero @RequestParam (name = "from",defaultValue = "0") Integer from,
                                       @Positive @RequestParam (name = "size",defaultValue = "10") Integer size) {
int page = from/size;
final PageRequest pageRequest=PageRequest.of(page, size, Sort.by("created").descending());
        return itemRequestService.getAllItemsRequests(userId,pageRequest);
    }
    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                      @PathVariable("requestId") long requestId) {
        return itemRequestService.getItemRequestsById(userId, requestId);
    }


    @PostMapping
    public ItemRequestDto add( @RequestHeader(ConstantsProject.USER_ID) Long userId,
                       @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addNewItemRequest(itemRequestDto, userId);
    }


    }



