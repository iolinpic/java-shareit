package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final EntityManager entityManager;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "john", "j@example.ru"));
        itemDto = itemService.createItemByUser(userDto.getId(), new ItemDto(null, "item", "desc",
                true, null, null, null, null));
    }

    @Test
    void getItemsByOwnerId() {
        List<ItemDto> items = itemService.getItemsByOwnerId(userDto.getId());

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemDto.getId(), items.getFirst().getId());
    }

    @Test
    void search() {
        List<ItemDto> items = itemService.search(userDto.getId(), "item");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemDto.getId(), items.getFirst().getId());

        items = itemService.search(userDto.getId(), "");
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void getItem() {
        ItemDto item = itemService.getItem(userDto.getId(), itemDto.getId());

        assertNotNull(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
    }

    @Test
    void createItemByUser() {
        ItemDto item = new ItemDto(null, "item uniq", "desc",
                true, null, null, null, null);
        item = itemService.createItemByUser(userDto.getId(), item);

        TypedQuery<Item> query = entityManager
                .createQuery("SELECT i from Item as i where i.name = :name", Item.class);
        Item result = query.setParameter("name", item.getName()).getSingleResult();

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void createItemWithRequest(){
        ItemRequestDto request = new ItemRequestDto(null,"request",null,null,null);
        request = itemRequestService.create(userDto.getId(), request);
        ItemDto item = new ItemDto(null, "item uniq", "desc",
                true, null, null, null, request.getId());
        item = itemService.createItemByUser(userDto.getId(), item);

        TypedQuery<Item> query = entityManager
                .createQuery("SELECT i from Item as i where i.id = :id", Item.class);
        Item result = query.setParameter("id", item.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(request.getId(), result.getRequest().getId());
    }

    @Test
    void wrongUserUpdateFal() {
        UserDto wrongUser = userService.add(new UserDto(null, "john3", "j3@example.ru"));

        ItemDto item = new ItemDto(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                false, null, null, null, null);


        assertThrows(ItemNotFoundException.class,
                () -> itemService.updateUserItem(wrongUser.getId(), itemDto.getId(), item));
    }

    @Test
    void updateUserItem() {
        ItemDto item = new ItemDto(itemDto.getId(), null, null,
                false, null, null, null, null);

        item = itemService.updateUserItem(userDto.getId(), itemDto.getId(), item);
        TypedQuery<Item> query = entityManager
                .createQuery("SELECT i from Item as i where i.id = :id", Item.class);
        Item result = query.setParameter("id", item.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertFalse(result.getAvailable());

        item = new ItemDto(itemDto.getId(), "new item", null,
                null, null, null, null, null);

        item = itemService.updateUserItem(userDto.getId(), itemDto.getId(), item);
        result = query.setParameter("id", item.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(item.getName(), result.getName());

        item = new ItemDto(itemDto.getId(), null, "new description",
                null, null, null, null, null);

        item = itemService.updateUserItem(userDto.getId(), itemDto.getId(), item);
        result = query.setParameter("id", item.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void createComment() {
        CommentDto comment = new CommentDto(null, "comment", null, null);
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        LocalDateTime nowPlusDay = now.minusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);
        bookingService.confirmBooking(userDto.getId(), bookingDto.getId(), true);

        comment = itemService.createComment(userDto.getId(), itemDto.getId(), comment);

        TypedQuery<Comment> query = entityManager
                .createQuery("select c from Comment as c where c.text = :text", Comment.class);
        Comment result = query.setParameter("text", comment.getText()).getSingleResult();

        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
    }
}