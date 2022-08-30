package ru.practicum.shareit.user.dto;

import lombok.*;

import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
    @Email(groups = {Create.class, Update.class}, message = "Wrong format email")
    @NotNull(groups = {Create.class})
    private String email;
    private String name;

}
