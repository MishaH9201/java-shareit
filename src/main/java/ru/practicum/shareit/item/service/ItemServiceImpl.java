package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDtoForComments> getItems(long userId) {
        List<Comment> comments=commentRepository.findByAuthorId(userId);
        log.info("Get Items");
        return repository.findByOwnerId(userId)
                .stream()
                .map(o -> ItemMapper.toItemDtoForComments(o,comments))
               // .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDtoForComments getItemById(Long userId, Long itemId) {
        List<Comment> comments=commentRepository.findByAuthorId(userId);
        log.info("Get Item by id");
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        return ItemMapper.toItemDtoForComments(item,comments);
    }

    @Override
    public Item addNewItem(ItemDto itemDto, Long userId) {
       User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
       Item item=ItemMapper.toItem(itemDto,user);
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Item itemUpdate=repository.findById(item.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Item not found"));
        if (userId != itemUpdate.getOwner().getId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Item belongs to another user");
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
        log.info("Update Items");
        return repository.save(itemUpdate);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return repository.search(text)
                    .stream()
                   .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
       }
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long userId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST," "));
        User creator = userRepository.findById(userId)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Booking booking = bookingRepository.findBookingForCheck(userId, itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST," "));

      //  if (booking.isEmpty() || booking.get().getEnd().isAfter(LocalDateTime.now()))
      //      throw new BadRequestException("Этот пользователь не может оставить комментарий");
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setItem(item);
        commentDto.setAuthor(creator);
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto));
        log.info("Пользователь с id={} добавил комментарий к вещи с id={}", userId, itemId);
        return CommentMapper.toCommentDto(comment);
    }
}
