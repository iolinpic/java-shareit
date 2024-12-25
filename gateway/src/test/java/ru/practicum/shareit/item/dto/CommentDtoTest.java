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
class CommentDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidationSuccess() throws Exception {
        CommentDto payload = new CommentDto(1L, "test", null, null);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(payload);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFail() throws Exception {
        CommentDto payload = new CommentDto(1L, null, null, null);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(payload, Create.class);
        assertFalse(violations.isEmpty());
    }
}