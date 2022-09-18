package ru.practicum.shareit.requests.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addNewItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        ItemRequest itemRequest = repository.save(ItemRequestMapper.toItemRequest(itemRequestDto, user));
        return ItemRequestMapper.toItemRequestDto(itemRequest, addItemsForRequest(itemRequest.getId()));

    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return repository.findByRequestorId(userId)
                .stream()
                .map(o -> ItemRequestMapper.toItemRequestDto(o, addItemsForRequest(o.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getItemRequestsById(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        ItemRequest request = repository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        return ItemRequestMapper.toItemRequestDto(request, addItemsForRequest(requestId));
    }

    @Override
    public List<ItemRequestDto> getAllItemsRequests(long userId, PageRequest pageRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return repository.findAll(pageRequest)
                .stream()
                .map(o -> ItemRequestMapper.toItemRequestDto(o, addItemsForRequest(o.getId())))
                .filter(o ->!o.getRequestor().equals(userId))
                .collect(Collectors.toList());
    }

    private List<ItemDto> addItemsForRequest(Long requestId) {
        return itemRepository.findByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
