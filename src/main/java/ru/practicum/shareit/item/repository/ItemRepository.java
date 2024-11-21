package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> findAllByOwnerId(Long userId);

    List<Item> findByText(String text);

    Item findById(Long id);

    Item add(Item item);

    Item update(Item item);
}
