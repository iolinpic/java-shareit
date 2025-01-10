package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingValidationException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.predicate.BookingPredicates;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingValidationException("interval between start and end cannot be 0");
        }
        User user = userExistCheckAndLoad(userId);
        Item item = itemExistCheckAndLoad(bookingDto.getItemId());
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long id) {
        User user = userExistCheckAndLoad(userId);
        Booking booking = bookingExistCheckAndLoad(id);
        if (booking.getBooker().equals(user) || booking.getItem().getOwner().equals(user)) {
            return BookingMapper.toDto(booking);
        }
        throw new BookingValidationException("access for details denied");
    }

    @Override
    public BookingDto confirmBooking(Long userId, Long id, Boolean confirm) {
//        User user = userExistCheckAndLoad(userId);
        Booking booking = bookingExistCheckAndLoad(id);
        if (booking.getItem().getOwner().getId().equals(userId) && booking.getStatus().equals(BookingStatus.WAITING)) {
            if (confirm) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return BookingMapper.toDto(bookingRepository.save(booking));
        }
        throw new BookingValidationException("access for details denied");
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        User user = userExistCheckAndLoad(userId);
        Iterable<Booking> result = bookingRepository.findAll(BookingPredicates.bookerAndState(user.getId(), state),
                BookingPredicates.orderByStart());
        return StreamSupport.stream(result.spliterator(), false).map(BookingMapper::toDto).toList();
    }


    @Override
    public List<BookingDto> getUserItemBookings(Long userId, BookingState state) {
        User user = userExistCheckAndLoad(userId);
        Iterable<Booking> result = bookingRepository.findAll(BookingPredicates.itemOwnerAndState(user.getId(), state),
                BookingPredicates.orderByStart());
        return StreamSupport.stream(result.spliterator(), false).map(BookingMapper::toDto).toList();
    }

    private Item itemExistCheckAndLoad(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("item not found"));
        if (!item.getAvailable()) {
            throw new BookingValidationException("item is not available");
        }
        return item;
    }

    private User userExistCheckAndLoad(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("This user doesn't exist"));
    }

    private Booking bookingExistCheckAndLoad(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("This booking doesn't exist"));
    }
}
