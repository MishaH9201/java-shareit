package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;
import ru.practicum.shareit.user.dto.UserDto;



@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Get users");
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody @Validated(Update.class) UserDto userDto) {
        log.info("Update  userId={}", id);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Delete  userId={}", id);
        return userClient.deleteUser(id);
    }
}
