package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest ir) {
        return ItemRequestDto.builder()
                .id(ir.getId())
                .description(ir.getDescription())
                .requester(UserMapper.toUserDto(ir.getRequester()))
                .created(ir.getCreated())
                .build();
    }


}
