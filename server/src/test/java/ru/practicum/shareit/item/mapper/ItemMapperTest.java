package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toDtoTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setDescription("test");
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");
        item.setOwner(user);

        ItemDto dto = ItemMapper.toDto(item);
        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertNull(dto.getRequestId());

        ItemRequest iRequest = new ItemRequest();
        iRequest.setId(1L);
        iRequest.setDescription("123");
        item.setRequest(iRequest);
        ItemDto dto2 = ItemMapper.toDto(item);
        assertNotNull(dto2);
        assertEquals(dto.getId(), dto2.getId());
        assertEquals(iRequest.getId(), dto2.getRequestId());
    }

    @Test
    void toShortDto() {
        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setDescription("test");
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@email.com");
        item.setOwner(user);
        ItemShortDto dto = ItemMapper.toShortDto(item);

        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(user.getId(),dto.getOwnerId());
    }
}