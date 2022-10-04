package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoForComments;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemDtoForComments> getItems(long userId, PageRequest pageRequest) {
        Page<Item> items = repository.findByOwnerIdOrderById(userId, pageRequest);
        List<ItemDtoForComments> itemDtoList = items.stream()
                .map(o -> ItemMapper.toItemDtoForComments(o,
                        addComments(o.getId()),
                        checkLastBooking(o.getId(), userId),
                        checkNextBooking(o.getId(), userId)))
                .collect(Collectors.toList());
        log.info("Get Items");
        return itemDtoList;
    }

    @Override
    public ItemDtoForComments getItemById(Long userId, Long itemId) {
        BookingDtoShort nextBooking = checkNextBooking(itemId, userId);
        BookingDtoShort lastBooking = checkLastBooking(itemId, userId);
        List<CommentDto> comments = addComments(itemId);
        log.info("Get Item by id");
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        return ItemMapper.toItemDtoForComments(item, comments, lastBooking, nextBooking);
    }

    @Override
    public Item addNewItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Item item = ItemMapper.toItem(itemDto, user);
        if(itemDto.getRequestId()!=null) {
            itemRequestRepository.findById(itemDto.getRequestId()).ifPresent(item::setRequest);
        }

        log.info("Add new Item");
        return repository.save(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        log.info("Delete Items");
        repository.deleteById(itemId);
    }

    @Override
    public Item updateItem(long userId, ItemDto item) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Item itemUpdate = repository.findById(item.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        if (userId != itemUpdate.getOwner().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ("Item belongs to another user"));
        }
        if (item.getName() != null) {
            itemUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        if(item.getRequestId() != null) {
            itemUpdate.setRequest(itemRequestRepository.findById(item.getRequestId()).orElse(null));
        }
        log.info("Update Items");
        return repository.save(itemUpdate);
    }

    @Override
    public List<ItemDto> search(String text,PageRequest pageRequest) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return repository.search(text, pageRequest)
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long userId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found "));
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Optional<Booking> booking = bookingRepository.getTopByItem_IdAndBooker_IdOrderByEndAsc(itemId, userId);
        if (booking.isEmpty() || booking.get().getEnd().isAfter(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time error");
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, item, creator));
        log.info("Add comment");
        return CommentMapper.toCommentDto(comment);
    }

    private BookingDtoShort checkLastBooking(long itemId, long ownerId) {
        Booking lastBooking = bookingRepository.findLastBookingWithItemAndOwner(itemId, ownerId).orElse(null);
        if (lastBooking == null) {
            return null;
        } else {
            return BookingMapper.toBookingDtoShort(lastBooking);
        }
    }

    private BookingDtoShort checkNextBooking(long itemId, long ownerId) {
        Booking lastBooking = bookingRepository.findNextBookingWithItemAndOwner(itemId, ownerId).orElse(null);
        if (lastBooking == null) {
            return null;
        } else {
            return BookingMapper.toBookingDtoShort(lastBooking);
        }
    }

    private List<CommentDto> addComments(Long itemId) {
        return commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
