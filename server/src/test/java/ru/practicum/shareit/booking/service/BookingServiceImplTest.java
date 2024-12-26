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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
    void createBooking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        TypedQuery<Booking> query = entityManager.createQuery("SELECT b from Booking as b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id",bookingDto.getId() ).getSingleResult();

        assertThat(booking, notNullValue());
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
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