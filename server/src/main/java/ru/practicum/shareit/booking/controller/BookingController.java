package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public BookingDto confirmBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long id,
                                     @RequestParam(name = "approved") Boolean approved) {
        return bookingService.confirmBooking(userId, id, approved);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUserItemBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.getUserItemBookings(userId, state);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return bookingService.getBooking(userId, id);
    }
}
