package com.thebreakingbugs.polaris_back_end.tasks.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status = TaskStatus.PENDING;
    private String moduleId;
    private String ownerId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Task(String title, String description, LocalDateTime dueDate, String moduleId, String ownerId) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.moduleId = moduleId;
        this.ownerId = ownerId;
    }
}