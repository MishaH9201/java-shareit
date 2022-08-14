package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto convert(User user){
      /*  UserDto userDto =new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;*/
       return new UserDto(user.getId(),
                user.getEmail(),
                user.getName());

    }
}
