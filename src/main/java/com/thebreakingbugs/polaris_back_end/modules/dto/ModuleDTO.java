package com.thebreakingbugs.polaris_back_end.modules.dto;

import jakarta.validation.constraints.NotBlank;

public record ModuleDTO(
    @NotBlank(message = "Module name cannot be blank")
    String name,
    String description,
    String color) {
}
