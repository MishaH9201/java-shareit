package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    void getAllUsers() {
        User user1 = userService.saveUser(new User(1L, "a@a.ru", "Ed"));
        User user2 = userService.saveUser(new User(2L, "b@a.ru", "Edy"));
        TypedQuery<User> query = em.createQuery("Select u from User u", User.class);
        List<User> usersTest = query.getResultList();
        assertThat(usersTest.size(), equalTo(2));
        assertThat(usersTest.get(0).getEmail(), equalTo(user1.getEmail()));
        assertThat(usersTest.get(0).getName(), equalTo(user1.getName()));
        assertThat(usersTest.get(1).getEmail(), equalTo(user2.getEmail()));
        assertThat(usersTest.get(1).getName(), equalTo(user2.getName()));
    }
}
