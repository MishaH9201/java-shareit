package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
        return repository.findByUserId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        log.info("Get Item by id");
        return repository.getItemById(userId, itemId);
    }

    @Override
    public Item addNewItem(Item item) {
        userRepository.findById(item.getUserId());
        log.info("Add new Item");
        return repository.save(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        log.info("Delete Items");
        repository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public Item updateItem(long userId, Item item) {
        userRepository.findById(userId);
        log.info("Update Items");
        return repository.updateItem(userId, item);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return repository.getAll()
                    .stream()
                    .filter(e -> filteringForSearch(e, text))
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    private Boolean filteringForSearch(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase())
                || item.getDescription().toLowerCase().contains(text.toLowerCase())
                && item.getAvailable();
    }
}
