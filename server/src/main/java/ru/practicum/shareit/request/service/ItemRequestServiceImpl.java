package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto dto) {
        User user = userExistCheckAndLoad(userId);
        ItemRequest itemRequest = ItemRequest.builder()
                .description(dto.getDescription())
                .requester(user)
                .created(LocalDateTime.now())
                .build();
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getById(Long userId, Long requestId) {
        ItemRequest item = itemRequestRepository.findItemRequestById()
        return ItemRequestMapper.toDto(item);
    }

    @Override
    public List<ItemRequestDto> getAll(Long userId) {
        itemRequestRepository.findAllItemRequestByRequesterId(Long userId);
        return List.of();
    }

    @Override
    public List<ItemRequestDto> getAllByUserId(Long userId) {
        return List.of();
    }

    private User userExistCheckAndLoad(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("This user doesn't exist"));
    }
}
