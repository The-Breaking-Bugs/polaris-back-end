package com.thebreakingbugs.polaris_back_end.tasks.controller;

import com.thebreakingbugs.polaris_back_end.tasks.dto.CreateTaskDTO;
import com.thebreakingbugs.polaris_back_end.tasks.dto.UpdateTaskDTO;
import com.thebreakingbugs.polaris_back_end.tasks.model.Task;
import com.thebreakingbugs.polaris_back_end.tasks.model.TaskStatus;
import com.thebreakingbugs.polaris_back_end.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestHeader("X-User-Id") String ownerId,
                       @RequestBody @Valid CreateTaskDTO request) {
        Task task = new Task(request.title(), request.description(), request.dueDate(), request.moduleId(), ownerId);
        
        return taskService.create(task);
    }

    @GetMapping
    public List<Task> list(@RequestHeader("X-User-Id") String ownerId,
                           @RequestParam(defaultValue = "PENDING") String status) {
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
        
        return taskService.listByStatus(ownerId, taskStatus);
    }
	
    @GetMapping("/{id}")
    public Task getDetails(@RequestHeader("X-User-Id") String ownerId,
                           @PathVariable String id) {
        return taskService.getDetails(id, ownerId);
    }

    @PutMapping("/{id}")
    public Task update(@RequestHeader("X-User-Id") String ownerId,
                       @PathVariable String id,
                       @RequestBody @Valid UpdateTaskDTO request) {
        return taskService.update(id, request, ownerId);
    }

    @PatchMapping("/{id}/status")
    public void toggleStatus(@RequestHeader("X-User-Id") String ownerId,
                             @PathVariable String id) {
        taskService.toggleStatus(id, ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader("X-User-Id") String ownerId,
                       @PathVariable String id) {
        taskService.delete(id, ownerId);
    }
}