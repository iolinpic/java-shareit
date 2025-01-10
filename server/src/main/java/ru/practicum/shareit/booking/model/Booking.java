package ru.practicum.shareit.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
}
