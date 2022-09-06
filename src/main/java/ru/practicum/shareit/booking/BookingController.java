package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
   /* @GetMapping
    public List<ItemDto> get(@RequestHeader(ItemController.USER_ID) long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(ItemController.USER_ID) long userId,
                           @PathVariable("itemId") long itemId) {
        itemService.getItemById(userId, itemId);
        return ItemMapper.toItemDto(itemService.getItemById(userId, itemId));
    }*/

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
    public BookingDtoForUpdate findBookingById(//@RequestHeader(ItemController.USER_ID) Long userId,
                                               @PathVariable long bookingId) {
        return bookingService.findBookingById(bookingId);
    }

    @GetMapping
    public List<BookingDtoForUpdate> findAllBookingsByUserId(@RequestHeader(ItemController.USER_ID) Long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUserId(userId,state);
    }
    @GetMapping("/owner")
    public List<BookingDtoForUpdate> findAllBookingsForItemsUser(@RequestHeader(ItemController.USER_ID) Long userId,
                                                                 @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsForItemsUser(userId, state);
    }
}
