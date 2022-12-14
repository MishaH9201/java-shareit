package ru.practicum.shareit.item.repository;


import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private Item item1, item2;
    private User user1, user2;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        itemRequest = new ItemRequest(1L, "Что-то пишущее", user2, LocalDateTime.now());
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        itemRequest = itemRequestRepository.save(itemRequest);
        item1 = new Item(1L, "Ручка", "Писательный инструмент", true, user1, itemRequest);
        item2 = new Item(2L, "Кошка", "Мяукательный инструмент", true, user2, null);
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void testSearchItemsWhenTextIsValid() {
        Page<Item> items = itemRepository.search("пИс", PageRequest.of(0, 10, Sort.by("id")));
        assertNotNull(items);
        assertEquals(1L, items.getContent().get(0).getId());
        assertEquals("Ручка", items.getContent().get(0).getName());
        assertEquals("Писательный инструмент", items.getContent().get(0).getDescription());
    }
}