package ru.practicum.shareit.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.dto.Create;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class UserDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidationSuccess() throws Exception {
        final UserDto payload = UserDto.builder()
                .email("email@email.com")
                .name("name")
                .build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(payload,Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFail() throws Exception {
        final UserDto payload = UserDto.builder().build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(payload, Create.class);
        assertFalse(violations.isEmpty());
    }
}