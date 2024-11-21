package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByOwnerId(Long userId);

    List<ItemDto> search(Long userId, String query);

    ItemDto getItem(Long userId, Long id);

    ItemDto createItemByUser(Long userId, ItemDto itemDto);

    ItemDto updateUserItem(Long userId, Long itemId, ItemDto itemDto);
}
