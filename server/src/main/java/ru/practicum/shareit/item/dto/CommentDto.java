package ru.practicum.shareit.item.dto;

import lombok.*;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
//    @NotBlank
//    @NotNull
    private String text;
    private String authorName;
    private LocalDateTime created;
}
