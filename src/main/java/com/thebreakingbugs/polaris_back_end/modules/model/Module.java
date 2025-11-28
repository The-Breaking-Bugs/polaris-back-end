package com.thebreakingbugs.polaris_back_end.modules.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "modules")
@CompoundIndex(name = "owner_name_uniqueness", def = "{'ownerId' : 1, 'name': 1}", unique = true)
public class Module {
    @Id
    private String id;

    @NotBlank(message = "Module name cannot be blank")
    private String name;

    private String description;

    private String color;

    private Boolean archived = false;

    @Indexed
    @NotBlank(message = "Owner ID cannot be blank")
    private String ownerId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Module() {
    }

    public Module(String name, String description, String color, String ownerId) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.ownerId = ownerId;
        this.archived = false;
    }
}
