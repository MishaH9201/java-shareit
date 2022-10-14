package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    ItemRequest itemRequest;
    User user1, user2;
    Item item1, item2;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        itemRequest = new ItemRequest(1L, "Что-то пишущее", user2, LocalDateTime.now());
        item1 = new Item(1L, "Ручка", "Писательный инструмент", true, user1, itemRequest);
        item2 = new Item(2L, "Карандаш", "Писательный инструмент", true, user2, null);

    }

    @Test
    void getItems() {
        user1 = userService.saveUser(user1);
        user2 = userService.saveUser(user2);
        Item itemTest1 = itemService.addNewItem(ItemMapper.toItemDto(item1), user1.getId());
        Item itemTest2 = itemService.addNewItem(ItemMapper.toItemDto(item2), user2.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i", Item.class);
        List<Item> itemsTest = query.getResultList();
        assertThat(itemsTest.size(), equalTo(2));
        assertThat(itemsTest.get(0).getName(), equalTo(item1.getName()));
        assertThat(itemsTest.get(0).getDescription(), equalTo(item1.getDescription()));
        assertThat(itemsTest.get(1).getName(), equalTo(item2.getName()));
        assertThat(itemsTest.get(1).getDescription(), equalTo(item2.getDescription()));
    }

}
