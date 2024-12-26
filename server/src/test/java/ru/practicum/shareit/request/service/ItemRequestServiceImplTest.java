package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final EntityManager entityManager;
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    private UserDto userDto;

    private ItemRequestDto itemRequestDto;


    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "john", "j@example.ru"));
        itemRequestDto = itemRequestService.create(userDto.getId(), new ItemRequestDto(null,
                "item request", null, null, null));
    }

    @Test
    void create() {
        ItemRequestDto dto = new ItemRequestDto(null, "item request uniq",
                null, null, null);

        dto = itemRequestService.create(userDto.getId(), dto);

        TypedQuery<ItemRequest> query = entityManager
                .createQuery("SELECT i from ItemRequest as i where i.description = :desc", ItemRequest.class);
        ItemRequest result = query.setParameter("desc", dto.getDescription()).getSingleResult();

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDescription(), result.getDescription());
    }

    @Test
    void getById() {
        ItemRequestDto dto = itemRequestService.getById(userDto.getId(), itemRequestDto.getId());

        assertNotNull(dto);
        assertEquals(itemRequestDto.getId(), dto.getId());
        assertEquals(itemRequestDto.getDescription(), dto.getDescription());
    }

    @Test
    void getAll() {
        List<ItemRequestDto> dtos = itemRequestService.getAll(userDto.getId() + 1);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(itemRequestDto.getId(), dtos.getFirst().getId());
    }

    @Test
    void getAllByUserId() {
        List<ItemRequestDto> dtos = itemRequestService.getAllByUserId(userDto.getId());

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(itemRequestDto.getId(), dtos.getFirst().getId());
    }
}