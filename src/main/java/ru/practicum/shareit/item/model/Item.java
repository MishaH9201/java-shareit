package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
/*
CREATE TABLE IF NOT EXISTS users (
                                     user_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(50) NOT NULL,
                                     email VARCHAR(50) NOT NULL UNIQUE,
                                     CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS items (
                                     item_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL,
                                     description VARCHAR(50) ,
                                     available boolean,
                                     owner int references users(user_id),
                                     request int references requests(request_id)
);
CREATE TABLE IF NOT EXISTS requests (
                                    request_id int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
                                    description VARCHAR(50) ,
                                    requestor int references users(user_id),
                                    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        CONSTRAINT pk_request PRIMARY KEY (request_id),
                                        CONSTRAINT FK_REQUEST_ON_REQUESTER FOREIGN KEY (request_id) REFERENCES users (user_id)
);CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        start_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        end_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        item_id BIGINT,
                                        booker_id BIGINT,
                                        approved BOOLEAN NOT NULL,
                                        canceled BOOLEAN NOT NULL,
                                        CONSTRAINT pk_booking PRIMARY KEY (id),
                                        CONSTRAINT FK_BOOKING_ON_BOOKER FOREIGN KEY (booker_id) REFERENCES users (id),
                                        CONSTRAINT FK_BOOKING_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        text VARCHAR(4000) NOT NULL,
                                        item_id BIGINT,
                                        author_id BIGINT,
                                        created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        CONSTRAINT pk_comment PRIMARY KEY (id),
                                        CONSTRAINT FK_COMMENT_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id),
                                        CONSTRAINT FK_COMMENT_ON_ITEM FOREIGN KEY (item_id) REFERENCES items (id)
);
 */