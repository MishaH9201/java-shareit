package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.User;

import javax.validation.constraints.Digits;

/**
 * // TODO .
 */
public class ItemDto {
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private String request;
}
