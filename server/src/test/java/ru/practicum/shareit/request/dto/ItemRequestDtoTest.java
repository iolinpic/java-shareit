package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    public void testSerialize() throws Exception {
        final ItemRequestDto userDto = new ItemRequestDto(
                1L,
                "name",
                null,
                null,
                null
        );
        JsonContent<ItemRequestDto> json = jacksonTester.write(userDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"description\":\"2024-12-22T21:04:20\"}";
        ItemRequestDto dto = jacksonTester.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1);
    }

}