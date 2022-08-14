package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;


@Data
@AllArgsConstructor
public class User {
    private Long id;
   // @Email
    private String email;
    private String name;
}
