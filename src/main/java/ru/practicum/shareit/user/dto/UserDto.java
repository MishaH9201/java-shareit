package ru.practicum.shareit.user.dto;

import lombok.*;

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
    @Email(message = "Wrong format email")
    @NotNull
    private String email;
    private String name;

}
