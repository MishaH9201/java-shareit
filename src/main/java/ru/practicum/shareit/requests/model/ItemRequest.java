package ru.practicum.shareit.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor")
    private User requestor;
    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd, hh:mm:ss")
    private LocalDateTime created;
}
