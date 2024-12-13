package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.util.List;

public interface ItemService {
    List<ItemOwnerDto> getItemsByOwnerId(Long userId);

    List<ItemDto> search(Long userId, String query);

    ItemDto getItem(Long userId, Long id);

    ItemDto createItemByUser(Long userId, ItemDto itemDto);

    ItemDto updateUserItem(Long userId, Long itemId, ItemDto itemDto);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);
}
