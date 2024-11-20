package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItemsByOwnerId(Long userId) {
        userRepository.getUser(userId);
        return itemRepository.findAllByOwnerId(userId).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> search(Long userId, String query) {
        userRepository.getUser(userId);
        return itemRepository.findByText(query).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public ItemDto getItem(Long userId, Long id) {
        userRepository.getUser(userId);
        return ItemMapper.toDto(itemRepository.findById(id));
    }

    @Override
    public ItemDto createItemByUser(Long userId, ItemDto itemDto) {
        User owner = userRepository.getUser(userId);
        Item newItem = ItemMapper.fromDto(itemDto);
        newItem.setOwner(owner);
        return ItemMapper.toDto(itemRepository.add(newItem));
    }

    @Override
    public ItemDto updateUserItem(Long userId, Long itemId, ItemDto itemDto) {
        User owner = userRepository.getUser(userId);
        Item item = itemRepository.findById(itemId);
        if (!item.getOwner().equals(owner)) {
            throw new ItemNotFoundException("This user doesn't own this item");
        }
        Item updateItem = ItemMapper.fromDto(itemDto);
        updateItem.setOwner(owner);
        if (updateItem.getId() == null) {
            updateItem.setId(item.getId());
        }
        if (updateItem.getName() == null) {
            updateItem.setName(item.getName());
        }
        if (updateItem.getDescription() == null) {
            updateItem.setDescription(item.getDescription());
        }
        if (updateItem.getAvailable() == null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toDto(itemRepository.update(updateItem));
    }
}
