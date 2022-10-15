package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
        if (user.equals(item.getOwner())) {
            log.info("User {} can't pick up your item", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can't pick up your item");
        }
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (end.isBefore(start)) {
            log.info("Time error");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time error");
        }
        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not possible");
        }
        Booking booking = BookingMapper.toBooking(bookingDto,user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        log.info("Add new Booking");
        return repository.save(booking);
    }

    @Override
    public Booking update(Long userId, long bookingId, boolean approved) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Only the owner can confirm");
        }
        if (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.REJECTED) {
            log.info("Status cannot be changed");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "\n" +
                    "Status cannot be changed");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        log.info("Update status booking");
        return repository.save(booking);
    }

    @Override
    public BookingDtoForUpdate findBookingById(long bookingId, long userId) {
        BookingDtoForUpdate bookingDto = BookingMapper.toBookingDtoForUpdate(repository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found")));
        log.info("Get booking");
        if (userId != bookingDto.getItem().getOwner().getId() && userId != bookingDto.getBooker().getId()) {
            log.info("User {} can't get booking", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User can't get booking");
        }
        return bookingDto;
    }

    @Override
    public List<BookingDtoForUpdate> findAllBookingsByUserId(Long userId, String state, PageRequest pageRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Page<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = repository.findAllByBookerId(userId, pageRequest);
                break;
            case "CURRENT":
                bookings = repository.findCorrentBookingsByBookerId(userId, pageRequest);
                break;
            case "PAST":
                bookings = repository.findPastBookingsByBookerId(userId, pageRequest);
                break;
            case "FUTURE":
                bookings = repository.findUpcomingBookingsByBookerId(userId, pageRequest);
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageRequest);
                break;
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageRequest);
                break;
            default:
                throw new BedRequestException("Unknown state: " + state);
        }
        log.info("Get booking by userId " + userId);
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForUpdate)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoForUpdate> findAllBookingsForItemsUser(Long userId, String state, PageRequest pageRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Page<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = repository.findBookingsItemsUser(userId, pageRequest);
                break;
            case "CURRENT":
                bookings = repository.findCurrentBookingsItemsUser(userId, pageRequest);
                break;
            case "PAST":
                bookings = repository.findPastBookingsItemsUser(userId, pageRequest);
                break;
            case "FUTURE":
                bookings = repository.findUpcomingBookingsItemsUser(userId, pageRequest);
                break;
            case "WAITING":
                bookings = repository.findByItemOwnerIdAndStatusWaiting(userId, BookingStatus.WAITING, pageRequest);
                break;
            case "REJECTED":
                bookings = repository.findByItemOwnerIdAndStatusRejected(userId, BookingStatus.REJECTED, pageRequest);
                break;
            default:
                throw new BedRequestException("Unknown state: " + state);
        }
        log.info("Get booking by item");
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForUpdate)
                .collect(Collectors.toList());
    }

}


