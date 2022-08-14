package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("User get");
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody User user) {
        UserDto userDto=UserMapper.convert(user);
        log.info("Save User");
        return userService.saveUser(userDto);
    }
    @PatchMapping("/{id}")
    public UserDto updateUser( @PathVariable Long id, @Valid @RequestBody User user){
        UserDto userDto=UserMapper.convert(user);
        log.info("Update User");
        return userService.updateUser(id,userDto);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Delete user");
        userService.deleteUser(id);
    }
}
