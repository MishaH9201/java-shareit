package ru.practicum.shareit.requests.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private User user1;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        userRepository.save(user1);
        itemRequest = new ItemRequest(1L, "Что-то пишущее", user1, LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findByRequestorIdWhenIdIsValid() {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(1L);
        assertNotNull(itemRequests);
        assertEquals(1L, itemRequests.get(0).getId());
        assertEquals("Что-то пишущее", itemRequests.get(0).getDescription());
    }
}