package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    @Test
    public void testSerialize() throws Exception {
        final ItemDto userDto = new ItemDto(
                1L,
                "name",
                "info@mail.ru",
                true,
                null,
                null,
                null,
                null
        );
        JsonContent<ItemDto> json = jacksonTester.write(userDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"name\":\"2024-12-22T21:04:20\",\"description\":\"info@mail.ru\",\"available\":true}";
        ItemDto dto = jacksonTester.parseObject(json);
        assertThat(dto.getId()).isEqualTo(1);
    }

}