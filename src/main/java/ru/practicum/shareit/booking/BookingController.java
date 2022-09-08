package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@Valid @RequestHeader(ItemController.USER_ID) Long userId,
                          @Valid @RequestBody BookingDto bookingDto) {
        Booking booking = bookingService.save(bookingDto, userId);
        return BookingMapper.toBookingDto(booking);
    }


    @PatchMapping("/{bookingId}")
    public BookingDtoForUpdate update(@RequestHeader(ItemController.USER_ID) Long userId,
                                      @PathVariable long bookingId,
                                      @RequestParam(value = "approved") boolean approved) {
        Booking booking = bookingService.update(userId, bookingId, approved);
        return BookingMapper.toBookingDtoForUpdate(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoForUpdate findBookingById(@RequestHeader(ItemController.USER_ID) Long userId,
                                               @PathVariable long bookingId) {
        return bookingService.findBookingById(bookingId,userId);
    }

    @GetMapping
    public List<BookingDtoForUpdate> findAllBookingsByUserId(@RequestHeader(ItemController.USER_ID) Long userId,
                                                             @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoForUpdate> findAllBookingsForItemsUser(@RequestHeader(ItemController.USER_ID) Long userId,
                                                                 @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsForItemsUser(userId, state);
    }
}
