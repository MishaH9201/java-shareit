package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    Item item1, item2;
    User user1, user2;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "email1@email.com", "Эдуард");
        user2 = new User(2L, "email2@email.com", "Станислав");
        userRepository.save(user1);
        userRepository.save(user2);
        item1 = new Item(1L, "Ручка", "Писательный инструмент", true, user1, null);
        item2 = new Item(2L, "Кошка", "Мяукательный инструмент", true, user2, null);
        itemRepository.save(item1);
        itemRepository.save(item2);
        comment = new Comment(1L, "Отличная ручка", item1, user2, LocalDateTime.now());
        commentRepository.save(comment);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findByItemId() {
        List<Comment> comments = commentRepository.findByItemId(1L);
        assertNotNull(comments);
        assertEquals(1L, comments.get(0).getId());
        assertEquals("Отличная ручка", comments.get(0).getText());
    }
}