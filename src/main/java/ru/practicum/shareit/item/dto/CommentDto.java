package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(groups = Create.class)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
