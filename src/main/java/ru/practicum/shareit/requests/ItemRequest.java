package ru.practicum.request;

import ru.practicum.user.User;

import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public class ItemRequest {
    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    private Long id;
    private String description;
    private User requestor;
    @FutureOrPresent
    private LocalDateTime created;
}
