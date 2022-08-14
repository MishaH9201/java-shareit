package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserRepositoryImpl;
import ru.practicum.shareit.user.dto.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems(long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public ItemDto addNewItem( ItemDto item) {
       userRepository.getUserById(item.getUserId());
        return repository.save(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public Item updateItem(long userId, ItemDto item) {
        return repository.updateItem(userId, item);
    }
}
