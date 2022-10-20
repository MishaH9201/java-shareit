package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getItemRequests(Long userId);

    ItemRequestDto getItemRequestsById(long userId, long requestId);

    List<ItemRequestDto> getAllItemsRequests(long userId, PageRequest pageRequest);
}
