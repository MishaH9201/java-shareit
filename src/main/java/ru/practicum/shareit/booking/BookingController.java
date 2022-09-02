package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;

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
        return BookingMapper.toBookingDto(bookingService.save(bookingDto, userId));
    }



    @PatchMapping("/{bookingId}?approved={approved}")
    public BookingDto update(@RequestHeader(ItemController.USER_ID) Long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        // Item item = ItemMapper.toItem(itemDto, userId);
       // itemDto.setId(itemId);
      //  itemService.updateItem(userId, itemDto);
     //   return ItemMapper.toItemDto(itemService.updateItem(userId, itemDto));
        return null;
    }

   /* @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        itemService.search(text);
        return itemService.search(text);
    }*/

}
