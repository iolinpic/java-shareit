package ru.practicum.shareit.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
class ItemDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidationSuccess() throws Exception {
        final ItemDto payload = ItemDto.builder()
                .name("Test")
                .description("Test")
                .available(true)
                .build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(payload, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFail() throws Exception {
        final ItemDto payload = ItemDto.builder().build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(payload, Create.class);
        assertFalse(violations.isEmpty());
    }
}