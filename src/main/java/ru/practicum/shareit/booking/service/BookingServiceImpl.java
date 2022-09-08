package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exseption.BedRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Booking save(BookingDto bookingDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        ;
        if (user.equals(item.getOwner())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can't pick up your item");
        }
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time error");
        }
        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not possible");
        }
        bookingDto.setBooker(user);
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return repository.save(booking);
    }

    @Override
    public Booking update(Long userId, long bookingId, boolean approved) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Only the owner can confirm");
        }
            if(booking.getStatus()==BookingStatus.APPROVED){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "\n" +
                        "Approved status cannot be changed");
            }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return repository.save(booking);
    }

    @Override
    public BookingDtoForUpdate findBookingById(long bookingId,long userId) {
      //  userRepository.findById(userId)
      //          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        BookingDtoForUpdate bookingDto = BookingMapper.toBookingDtoForUpdate(repository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found")));
        if(userId !=bookingDto.getItem().getOwner().getId() && userId != bookingDto.getBooker().getId()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User can't get booking");
        }return bookingDto;
    }

    @Override
    public List<BookingDtoForUpdate> findAllBookingsByUserId(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = repository.findByBookerId(userId)
                        .stream()
                        .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                        .collect(Collectors.toList());
                break;
            case "CURRENT":
                 bookings=repository.findCorrentBookingsByBookerId(userId);
                break;
            case "PAST":
                bookings=repository.findPastBookingsByBookerId(userId);
                break;
            case "FUTURE":
                bookings=repository.findUpcomingBookingsByBookerId(userId);
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
              throw new BedRequestException("Unknown state: "+state);
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForUpdate)
                .collect(Collectors.toList());
    }
    public  List<BookingDtoForUpdate> findAllBookingsForItemsUser(Long userId,String state){
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = repository.findBookingsItemsUser(userId);
                break;
            case "CURRENT":
                bookings = repository.findCurrentBookingsItemsUser(userId);
                break;
            case "PAST":
                bookings = repository.findPastBookingsItemsUser(userId);
                break;
            case "FUTURE":
                bookings = repository.findUpcomingBookingsItemsUser(userId);
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new BedRequestException("Unknown state: "+state);
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForUpdate)
                .collect(Collectors.toList());
    }
}


