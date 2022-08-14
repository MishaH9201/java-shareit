package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList())
                ;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("User get");
        return UserMapper.toUserDto(userService.getUserById(id));
    }

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody UserDto userDto) {
        User user=UserMapper.toUser(userDto);
        log.info("Save User");
        return UserMapper.toUserDto(userService.saveUser(user));
    }
    @PatchMapping("/{id}")
    public UserDto updateUser( @PathVariable Long id,  @RequestBody UserDto userDto){
        User user=UserMapper.toUser(userDto);
        log.info("Update User");
        return UserMapper.toUserDto(userService.updateUser(id,user));
       // return userService.updateUser(id,userDto);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Delete user");
        userService.deleteUser(id);
    }
}
