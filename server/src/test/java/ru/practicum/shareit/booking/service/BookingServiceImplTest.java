package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingValidationException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final EntityManager entityManager;

    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "john", "j@example.ru"));
        itemDto = itemService.createItemByUser(userDto.getId(), new ItemDto(null,
                "item", "description", true,
                null, null, null, null));
    }

    @Test
    void wrongIntervalTestFail() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = new BookingDto(null, now, now,
                itemDto.getId(), null, null, null);
        assertThrows(BookingValidationException.class, () -> bookingService.createBooking(userDto.getId(), bookingDto));
    }

    @Test
    void itemAvailableFalseTestFail() {
        ItemDto wrongItem = itemService.createItemByUser(userDto.getId(), new ItemDto(null,
                "item", "description", false,
                null, null, null, null));
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = new BookingDto(null, now, now.plusDays(1),
                wrongItem.getId(), null, null, null);

        assertThrows(BookingValidationException.class, () -> bookingService.createBooking(userDto.getId(), bookingDto));
    }

    @Test
    void createBooking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        TypedQuery<Booking> query = entityManager.createQuery("SELECT b from Booking as b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertThat(booking, notNullValue());
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
    }

    @Test
    void wrongUserGetFail() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = bookingService.createBooking(userDto.getId(), new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null));
        UserDto wrongUser = userService.add(new UserDto(null, "john3", "j3@example.ru"));

        assertThrows(BookingValidationException.class,
                () -> bookingService.getBooking(wrongUser.getId(), bookingDto.getId()));
    }

    @Test
    void getBookingViaItemOwner() {
        UserDto user = userService.add(new UserDto(null, "john23", "j322@example.ru"));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(user.getId(), bookingDto);

        BookingDto result = bookingService.getBooking(userDto.getId(), bookingDto.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getBooker(), equalTo(user));
    }

    @Test
    void getBooking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        BookingDto result = bookingService.getBooking(userDto.getId(), bookingDto.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getBooker(), equalTo(userDto));
    }

    @Test
    void wrongUserConfirmFail() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = bookingService.createBooking(userDto.getId(), new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null));
        UserDto wrongUser = userService.add(new UserDto(null, "john3", "j3@example.ru"));

        assertThrows(BookingValidationException.class,
                () -> bookingService.confirmBooking(wrongUser.getId(), bookingDto.getId(), true));
    }

    @Test
    void wrongIdBookingConfirmFail(){
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.confirmBooking(userDto.getId(), -1L, true));
    }
    @Test
    void wrongItemOwnerBookingConfirmFail(){
        UserDto user = userService.add(new UserDto(null, "john2", "j32@example.ru"));
        ItemDto item = itemService.createItemByUser(user.getId(), new ItemDto(null,
                "item", "description", true,
                null, null, null, null));
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.confirmBooking(userDto.getId(), item.getId(), true));

    }

    @Test
    void confirmBooking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        BookingDto result = bookingService.confirmBooking(userDto.getId(), bookingDto.getId(), true);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getStatus(), equalTo(BookingStatus.APPROVED));

        bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        result = bookingService.confirmBooking(userDto.getId(), bookingDto.getId(), false);
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getStatus(), equalTo(BookingStatus.REJECTED));

    }

    @Test
    void getUserBookings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        List<BookingDto> result = bookingService.getUserBookings(userDto.getId(), BookingState.ALL);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getId(), equalTo(bookingDto.getId()));

        result = bookingService.getUserBookings(userDto.getId(), BookingState.CURRENT);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));

        result = bookingService.getUserBookings(userDto.getId(), BookingState.FUTURE);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));

        result = bookingService.getUserBookings(userDto.getId(), BookingState.PAST);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));

        result = bookingService.getUserBookings(userDto.getId(), BookingState.WAITING);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));

        result = bookingService.getUserBookings(userDto.getId(), BookingState.REJECTED);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));
    }

    @Test
    void getUserItemBookings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        List<BookingDto> result = bookingService.getUserItemBookings(userDto.getId(), BookingState.ALL);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getId(), equalTo(bookingDto.getId()));
    }
}