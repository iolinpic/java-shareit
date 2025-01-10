package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.Create;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting items by owner id {}", userId);
        return itemClient.itemsByOwner(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(name = "text") String text) {
        log.info("Searching for items with text {}", text);
        return itemClient.search(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> createItemByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @Validated(Create.class) @RequestBody final ItemDto itemDto) {
        log.info("Creating item {}", itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateUserItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId,
                                                 @RequestBody final ItemDto itemDto) {
        log.info("Updating item {}", itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        log.info("Getting item {}", itemId);
        return itemClient.itemById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody final CommentDto comment) {
        log.info("Creating comment {}", comment);
        return itemClient.createComment(userId, itemId, comment);
    }

}
