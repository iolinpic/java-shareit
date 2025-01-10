package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService service;
    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private BookingDto dto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        dto = new BookingDto(
                1L,
                null,
                null,
                1L,
                BookingStatus.WAITING,
                null,
                null
        );
    }

    @Test
    void createBooking() throws Exception {
        when(service.createBooking(1L, dto)).thenReturn(dto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    void confirmBooking() throws Exception {
        when(service.confirmBooking(1L, 1L, true)).thenReturn(dto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    void getUserBookings() throws Exception {
        when(service.getUserBookings(eq(1L), any())).thenReturn(List.of(dto));
        mvc.perform(get("/bookings").content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(dto))));
    }

    @Test
    void getUserItemBookings() throws Exception {
        when(service.getUserItemBookings(eq(1L), any())).thenReturn(List.of(dto));
        mvc.perform(get("/bookings/owner").content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(dto))));
    }

    @Test
    void getBooking() throws Exception {
        when(service.getBooking(eq(1L), eq(1L))).thenReturn(dto);
        mvc.perform(get("/bookings/1").content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }
}