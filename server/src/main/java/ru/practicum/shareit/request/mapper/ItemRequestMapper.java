package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest ir) {
        return new ItemRequestDto(ir.getId(),
                ir.getDescription(), UserMapper.toUserDto(ir.getRequester()), ir.getCreated(), null);
    }


}
