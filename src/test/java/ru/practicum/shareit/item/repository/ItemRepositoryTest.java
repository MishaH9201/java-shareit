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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
     ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
   @Autowired
   ItemRequestRepository itemRequestRepository;
   Item item1, item2;
    User user1, user2;
    ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
         user1= new User(1L, "email1@email.com", "Эдуард");
         user2= new User(2L, "email2@email.com", "Станислав");
         itemRequest=new ItemRequest(1L,"Что-то пишущее",user2, LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest);
         item1=new Item(1l, "Ручка", "Писательный инструмент",true, user1,itemRequest);
         item2=new Item(2l, "Кошка", "Мяукательный инструмент",true, user2,null);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

@AfterEach
void afterEach() {
    userRepository.deleteAll();
    itemRepository.deleteAll();
    itemRequestRepository.deleteAll();
}

    @Test
    void search() {
        Page<Item> item = itemRepository.search("пИс", PageRequest.of(0,10, Sort.by("id")));
        assertNotNull(item);
        assertEquals(1L, item.getContent().get(0).getId());
        assertEquals("Ручка", item.getContent().get(0).getName());
        assertEquals("Писательный инструмент", item.getContent().get(0).getDescription());
    }

    @Test
    void findByOwnerIdOrderById() {
        Page<Item> item = itemRepository.findByOwnerIdOrderById(1L, PageRequest.of(0,10, Sort.by("id")));
        assertNotNull(item);
        assertEquals(1L, item.getTotalElements());
        assertEquals("Ручка", item.getContent().get(0).getName());
        assertEquals("Писательный инструмент", item.getContent().get(0).getDescription());
    }

    @Test
    void findByRequestId() {
        List<Item> item = itemRepository.findByRequestId(1L);
        assertNotNull(item);
        assertEquals(1L, item.get(0).getId());
        assertEquals("Ручка", item.get(0).getName());
        assertEquals("Писательный инструмент", item.get(0).getDescription());
    }
}