package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.user.model.User;


import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
