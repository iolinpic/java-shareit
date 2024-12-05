package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingDto bookingDto);

    BookingDto getBooking(Long userId, Long id);

    BookingDto confirmBooking(Long userId, Long id, Boolean confirm);

    List<BookingDto> getUserBookings(Long userId, BookingState state);

    List<BookingDto> getUserItemBookings(Long userId, BookingState state);
}
