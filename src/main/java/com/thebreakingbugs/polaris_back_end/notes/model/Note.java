package com.thebreakingbugs.polaris_back_end.notes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @TextIndexed(weight = 2) 
    private String title;

    @TextIndexed(weight = 1)
    private String content;

    @Indexed
    @NotBlank(message = "Module ID cannot be blank")
    private String moduleId;

    @Indexed
    @NotBlank(message = "Owner ID cannot be blank")
    private String ownerId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Note(String title, String content, String moduleId, String ownerId) {
        this.title = title;
        this.content = content;
        this.moduleId = moduleId;
        this.ownerId = ownerId;
    }
}
