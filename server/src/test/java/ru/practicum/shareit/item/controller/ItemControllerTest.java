package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService service;
    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto dto;
    private ItemOwnerDto ownerDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        dto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null,
                null,
                List.of(),
                null
        );
        ownerDto = new ItemOwnerDto(
                1L,
                "name",
                "description",
                true,
                null,
                null,
                List.of(),
                null
        );
    }

    @Test
    void getItemsByOwnerId() throws Exception {
        when(service.getItemsByOwnerId(1L)).thenReturn(List.of(ownerDto));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(ownerDto))));
    }

    @Test
    void search() throws Exception {
        when(service.search(eq(1L), any())).thenReturn(List.of(dto));
        mvc.perform(get("/items/search?text=text")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(dto))));
    }

    @Test
    void createItemByUser() throws Exception {
        when(service.createItemByUser(1L, dto)).thenReturn(dto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    void updateUserItem() throws Exception {
        when(service.updateUserItem(1L, 1L, dto)).thenReturn(dto);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(dto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    void getItem() throws Exception {
        when(service.getItem(1L, 1L)).thenReturn(dto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    void createComment() throws Exception {
        CommentDto cDto = new CommentDto(
                1L,
                "text",
                null,
                null
        );
        when(service.createComment(1L, 1L, cDto)).thenReturn(cDto);
        mvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(cDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(cDto)));
    }
}