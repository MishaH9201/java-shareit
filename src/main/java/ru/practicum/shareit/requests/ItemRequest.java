package ru.practicum.shareit.requests;

import ru.practicum.shareit.user.model.User;


import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
