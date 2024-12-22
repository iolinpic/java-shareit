package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotNull(groups = Create.class)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
