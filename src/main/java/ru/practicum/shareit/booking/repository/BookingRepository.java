package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {
    List<Booking> findByBookerId(Long userId);

    List<Booking> findByStatusAndBookerId(BookingStatus bookingStatus, Long userId);

    List<Booking> findByStartBeforeAndEndAfterAndBookerId(LocalDateTime start, LocalDateTime end, Long userId);

    List<Booking> findByEndBeforeAndBookerId(LocalDateTime end, Long userId);

    List<Booking> findByStartAfterAndBookerId(LocalDateTime start, Long userId);
}
