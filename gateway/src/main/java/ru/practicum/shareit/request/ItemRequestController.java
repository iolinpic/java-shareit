package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemRequestDto dto) {
        log.info("Create request: {}", dto);
        return itemRequestClient.createItemRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> userItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("User requests: {}", userId);
        return itemRequestClient.getAllUserItemRequest(userId);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Object> allItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("User requests");
        return itemRequestClient.getAllItemRequest(userId);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<Object> singleItemRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("requestId") Long requestId) {
        log.info("Single request: {}", requestId);
        return itemRequestClient.getItemRequest(userId, requestId);
    }
}
