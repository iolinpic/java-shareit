package ru.practicum.shareit.booking.predicate;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.QBooking;

import java.time.LocalDateTime;

public final class BookingPredicates {
    private BookingPredicates() {
    }

    private static BooleanExpression byBookerId(Long userId) {
        return QBooking.booking.booker.id.eq(userId);
    }

    private static BooleanExpression byItemOwnerId(Long userId) {
        return QBooking.booking.item.owner.id.eq(userId);
    }

    private static BooleanExpression byState(BookingState state) {
        if (state.equals(BookingState.WAITING)) {
            return QBooking.booking.status.eq(BookingStatus.WAITING);
        }
        if (state.equals(BookingState.REJECTED)) {
            return QBooking.booking.status.eq(BookingStatus.REJECTED);
        }
        if (state.equals(BookingState.CURRENT)) {
            return QBooking.booking.start.before(LocalDateTime.now()).and(QBooking.booking.end.after(LocalDateTime.now()));
        }
        if (state.equals(BookingState.FUTURE)) {
            return QBooking.booking.start.after(LocalDateTime.now());
        }
        if (state.equals(BookingState.PAST)) {
            return QBooking.booking.end.before(LocalDateTime.now());
        }
        return null;
    }

    public static Predicate bookerAndState(Long userId, BookingState state) {
        return byBookerId(userId).and(byState(state));
    }

    public static Predicate itemOwnerAndState(Long userId, BookingState state) {
        return byItemOwnerId(userId).and(byState(state));
    }

    public static OrderSpecifier<LocalDateTime> orderByStart() {
        return QBooking.booking.start.desc();
    }
}
