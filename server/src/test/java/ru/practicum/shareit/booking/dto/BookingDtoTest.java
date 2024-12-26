package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        final BookingDto bookItemRequestDto = new BookingDto(
                1L,
                now,
                nowPlusDay,
                1L,
                BookingStatus.WAITING,
                null,
                null
        );
        JsonContent<BookingDto> json = jacksonTester.write(bookItemRequestDto);
        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2024-12-22T21:04:20\",\"end\":\"2024-12-22T21:04:20\",\"status\":\"WAITING\"}";
        BookingDto dto = jacksonTester.parseObject(json);
        assertThat(dto.getItemId()).isEqualTo(1);
    }

}