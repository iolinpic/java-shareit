package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    public void testSerialize() throws Exception {
        final CommentDto userDto = CommentDto.builder()
                .id(1L)
                .text("test")
                .authorName("author")
                .build();
        JsonContent<CommentDto> json = jacksonTester.write(userDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"text\":\"2024-12-22T21:04:20\",\"author\":\"info@mail.ru\"}";
        CommentDto dto = jacksonTester.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1);
    }
}