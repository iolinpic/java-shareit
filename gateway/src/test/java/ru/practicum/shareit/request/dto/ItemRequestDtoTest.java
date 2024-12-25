package ru.practicum.shareit.request.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@JsonTest
class ItemRequestDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidationSuccess() throws Exception {
        final ItemRequestDto payload = ItemRequestDto.builder()
                .description("Test")
                .build();
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(payload);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFail() throws Exception {
        final ItemRequestDto payload = ItemRequestDto.builder().build();
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(payload);
        assertFalse(violations.isEmpty());
    }
}