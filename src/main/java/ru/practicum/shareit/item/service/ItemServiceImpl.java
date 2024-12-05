package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.exception.BookingValidationException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.predicate.BookingPredicates;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemOwnerDto> getItemsByOwnerId(Long userId) {
        userExistCheckAndLoad(userId);

        Iterable<Booking> bookings = bookingRepository.findAll(BookingPredicates.itemOwnerAndState(userId, BookingState.ALL), BookingPredicates.orderByStart());
        return itemRepository.findByOwnerId(userId).stream().map(ItemMapper::toOwnerDto)
                .peek((item) -> {
                    List<Booking> itemBooking = StreamSupport.stream(bookings.spliterator(), false)
                            .filter(b -> b.getItem().getId().equals(item.getId())).toList();
                    Booking previousBooking = itemBooking.stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                            .findFirst().orElse(null);
                    Booking nextBooking = itemBooking.reversed().stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now())).findFirst().orElse(null);


                    if (previousBooking != null) {
                        item.setLastBooking(previousBooking.getStart());
                    }
                    if (nextBooking != null) {
                        item.setNextBooking(nextBooking.getStart());
                    }
                    List<Comment> comments = commentRepository.findByItemId(item.getId());
                    item.setComments(comments.stream().map(CommentMapper::toDto).toList());
                })
                .toList();
    }

    @Override
    public List<ItemDto> search(Long userId, String query) {
        userExistCheckAndLoad(userId);
        if (query.isEmpty()) {
            return List.of();
        }
        return itemRepository.findByText(query).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public ItemDto getItem(Long userId, Long id) {
        ItemDto item = ItemMapper.toDto(itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("This item doesn't exist")));
        List<Comment> comments = commentRepository.findByItemId(id);
        item.setComments(comments.stream().map(CommentMapper::toDto).toList());
        return item;
    }

    @Override
    public ItemDto createItemByUser(Long userId, ItemDto itemDto) {
        User owner = userExistCheckAndLoad(userId);
        Item newItem = ItemMapper.fromDto(itemDto);
        newItem.setOwner(owner);
        return ItemMapper.toDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto updateUserItem(Long userId, Long itemId, ItemDto itemDto) {
        User owner = userExistCheckAndLoad(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("This item doesn't exist"));
        if (!item.getOwner().equals(owner)) {
            throw new ItemNotFoundException("This user doesn't own this item");
        }
        Item updateItem = ItemMapper.fromDto(itemDto);
        updateItem.setOwner(owner);
        if (updateItem.getId() == null) {
            updateItem.setId(item.getId());
        }
        if (updateItem.getName() == null) {
            updateItem.setName(item.getName());
        }
        if (updateItem.getDescription() == null) {
            updateItem.setDescription(item.getDescription());
        }
        if (updateItem.getAvailable() == null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toDto(itemRepository.save(updateItem));
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        Booking booking = bookingRepository
                .findOneByStatusAndBookerIdAndItemIdAndEndBefore(BookingStatus.APPROVED, userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new BookingValidationException("This booking doesn't exist"));
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .author(booking.getBooker())
                .item(booking.getItem())
                .created(LocalDateTime.now())
                .build();
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    private User userExistCheckAndLoad(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("This user doesn't exist"));
    }
}
