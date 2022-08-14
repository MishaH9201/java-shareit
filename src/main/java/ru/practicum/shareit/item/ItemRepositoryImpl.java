package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.dto.ItemDto;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<ItemDto>> items = new HashMap<>();

    @Override
    public List<ItemDto> findByUserId(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public ItemDto save(ItemDto item) {
        item.setId(getId());
        items.compute(item.getUserId()/*item.getOwner().getId()*/, (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if(items.containsKey(userId)) {
            List<ItemDto> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }
    @Override
    public Item updateItem(long userId, ItemDto item){
        ItemDto itemUpdate=items.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(e->e.getId()==item.getId())
                .findAny()
                .orElse(null);
        if(itemUpdate == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found");
        }
        if()
        return null;
    }

    private long getId() {
        long lastId = items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(ItemDto::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
