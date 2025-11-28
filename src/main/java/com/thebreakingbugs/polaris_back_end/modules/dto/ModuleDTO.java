package com.thebreakingbugs.polaris_back_end.modules.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModuleDTO {
    @NotBlank(message = "Module name cannot be blank")
    private String name;

    private String description;

    private String color;
}
