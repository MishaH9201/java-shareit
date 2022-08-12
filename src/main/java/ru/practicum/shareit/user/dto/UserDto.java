package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;

public class UserDto {
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
    @Email
    private String email;
    private String name;
}
