package com.thebreakingbugs.polaris_back_end.tasks.dto;

import java.time.LocalDateTime;

public record UpdateTaskDTO(
    String title,
    String description,
    LocalDateTime dueDate
) {}