package ru.practicum.shareit.item.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public List<Item> findByUserId(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Item> getAll() {
        return items.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }

    @Override
    public Item save(Item item) {
        item.setId(getId());
        items.compute(item.getUserId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    @Override
    public Item updateItem(long userId, Item item) {
        if (!items.containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Item updateItem = new Item();
        for (Item it : items.get(userId)) {
            if (it.getId() == item.getId()) {
                if (item.getName() != null) {
                    it.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    it.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    it.setAvailable(item.getAvailable());
                }
                updateItem = it;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
        }
        return updateItem;
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        Item item = items.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(e -> e.getId().equals(itemId))
                .findFirst()
                .orElse(null);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        } else {
            return item;
        }
    }

    private long getId() {
        long lastId = items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
