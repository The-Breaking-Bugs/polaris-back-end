package com.thebreakingbugs.polaris_back_end.modules.dto;

import jakarta.validation.constraints.Size;

public record UpdateModuleDTO(
    @Size(min = 1, message = "Name must not be empty")
    String name,
    String description,
    String color) {
}
