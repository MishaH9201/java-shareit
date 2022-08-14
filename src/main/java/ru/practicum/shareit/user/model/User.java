package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * // TODO .
 */
@Data
public class User {
    private Long id;
    @Email
    private String email;
    private String name;
}
