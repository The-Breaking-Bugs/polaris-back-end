package com.thebreakingbugs.polaris_back_end.tasks.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateTaskDTO(
    @NotBlank String title,
    String description,
    @NotNull @FutureOrPresent LocalDateTime dueDate,
    @NotBlank String moduleId
) {}