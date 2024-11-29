package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotEmpty(groups = Create.class)
    private String name;
    @NotEmpty(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
}
