package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exseption.BedRequestException;
import ru.practicum.shareit.util.ConstantsProject;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BedRequestException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.findAllBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsForItemsUser(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                                              @RequestParam(name = "state", defaultValue = "ALL", required =
                                                                      false) String stateParam,
                                                              @PositiveOrZero @RequestParam(defaultValue = "0", required =
                                                                      false) int from,
                                                              @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BedRequestException("Unknown state: " + stateParam));

        return bookingClient.findAllBookingsForItemsUser(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                      @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.add(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(ConstantsProject.USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findBookingById(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(ConstantsProject.USER_ID) Long userId,
                                         @PathVariable long bookingId,
                                         @RequestParam boolean approved) {
        log.info("Update booking {}, approved={}", bookingId, approved);
        return bookingClient.update(userId, bookingId, approved);
    }

}
