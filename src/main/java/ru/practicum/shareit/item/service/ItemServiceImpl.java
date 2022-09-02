package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems(long userId) {
        log.info("Get Items");
        return repository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        log.info("Get Item by id");
        return repository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @Override
    public Item addNewItem(ItemDto itemDto, Long userId) {
       User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
       Item item=ItemMapper.toItem(itemDto,user);
        log.info("Add new Item");
        return repository.save(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        log.info("Delete Items");
        repository.deleteById(itemId);
    }

    @Override
    public Item updateItem(long userId, ItemDto item) {
         userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Item itemUpdate=repository.findById(item.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found"));
        if (userId != itemUpdate.getOwner().getId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Item belongs to another user");
        }
            if (item.getName() != null) {
                itemUpdate.setName(item.getName());
            }
            if (item.getDescription() != null) {
                itemUpdate.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                itemUpdate.setAvailable(item.getAvailable());
            }
        log.info("Update Items");
        return repository.save(itemUpdate);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return repository.search(text)
                    .stream()
                   .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
       }

    }
}
