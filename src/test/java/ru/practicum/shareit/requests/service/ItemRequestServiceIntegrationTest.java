package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
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
public class ItemRequestServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemService itemService;
    ItemDto itemDto;
    ItemRequestDto itemRequestDto1, itemRequestDto2;
    User user1,user2;

    @BeforeEach
    void beforeEach(){
        itemDto = new ItemDto(1l, "Ручка","Писательный инструмент",true,1L);
        itemRequestDto1 = new ItemRequestDto(1L,"Good", LocalDateTime.now(),2L,null);
        itemRequestDto2 = new ItemRequestDto(2L,"Good pen", LocalDateTime.now(),2L,null);
    }

    @Test
    void getItemRequests(){
        user1 = userService.saveUser(new User(1L,"a@a.ru","Ed"));user2 = userService.saveUser(new User(2L,"b@a.ru","Edy"));
        Item item = itemService.addNewItem(itemDto,1L);
        ItemRequestDto itemRequest1 = itemRequestService.addNewItemRequest(itemRequestDto1,2L);
        ItemRequestDto itemRequest2 = itemRequestService.addNewItemRequest(itemRequestDto2,2L);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i", ItemRequest.class);
        List<ItemRequest> itemRequestsTest = query.getResultList();
        assertThat(itemRequestsTest.size(), equalTo(2));
        assertThat(itemRequestsTest.get(0).getDescription(), equalTo(itemRequest1.getDescription()));
        assertThat(itemRequestsTest.get(1).getDescription(), equalTo(itemRequest2.getDescription()));
    }
}
