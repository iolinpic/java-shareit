package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestDto dto);

    ItemRequestDto getById(Long userId, Long requestId);

    List<ItemRequestDto> getAll(Long userId);

    List<ItemRequestDto> getAllByUserId(Long userId);
}
