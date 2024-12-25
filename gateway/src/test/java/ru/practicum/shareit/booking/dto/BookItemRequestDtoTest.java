package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BookItemRequestDtoTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> jacksonTester;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        final BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(
                1L,
                now,
                nowPlusDay
        );
        JsonContent<BookItemRequestDto> json = jacksonTester.write(bookItemRequestDto);
        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2024-12-22T21:04:20\",\"end\":\"2024-12-22T21:04:20\"}";
        BookItemRequestDto dto = jacksonTester.parseObject(json);
        assertThat(dto.getItemId()).isEqualTo(1);
    }

    @Test
    public void testValidationSuccess() throws Exception {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusDay = now.plusDays(2);
        final BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(
                1L,
                now,
                nowPlusDay
        );
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFail() throws Exception {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        LocalDateTime nowPlusDay = now.plusDays(2);
        final BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(
                1L,
                now,
                nowPlusDay
        );
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);
        assertFalse(violations.isEmpty());
    }
}