package com.thebreakingbugs.polaris_back_end.notes.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNoteDTO(
    @NotBlank(message = "Note title cannot be blank")
    String title,
    String content) {
}
