package com.thebreakingbugs.polaris_back_end.tasks.service;

import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import com.thebreakingbugs.polaris_back_end.modules.repository.ModuleRepository;
import com.thebreakingbugs.polaris_back_end.tasks.dto.UpdateTaskDTO;
import com.thebreakingbugs.polaris_back_end.tasks.model.Task;
import com.thebreakingbugs.polaris_back_end.tasks.model.TaskStatus;
import com.thebreakingbugs.polaris_back_end.tasks.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;

    public TaskService(TaskRepository taskRepository, ModuleRepository moduleRepository) {
        this.taskRepository = taskRepository;
        this.moduleRepository = moduleRepository;
    }

    public Task create(Task task) {
        if (task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future or present");
        }

        Module module = moduleRepository.findById(task.getModuleId())
                .orElseThrow(() -> new NoSuchElementException("Module not found"));

        if (!module.getOwnerId().equals(task.getOwnerId())) {
            throw new IllegalArgumentException("Module does not belong to this user");
        }

        if (Boolean.TRUE.equals(module.getArchived())) {
            throw new IllegalArgumentException("Cannot create tasks for archived modules");
        }

        return taskRepository.save(task);
    }

    public List<Task> listByStatus(String ownerId, TaskStatus status) {
        if (status == TaskStatus.PENDING) {
            return taskRepository.findByOwnerIdAndStatusOrderByDueDateAsc(ownerId, status);
        } else {
            return taskRepository.findByOwnerIdAndStatusOrderByDueDateDesc(ownerId, status);
        }
    }

    public Task getDetails(String id, String ownerId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new SecurityException("User is not the owner of this task");
        }

        return task;
    }

    public Task update(String id, UpdateTaskDTO request, String ownerId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new SecurityException("User is not the owner of this task");
        }

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }

        return taskRepository.save(task);
    }

    public void toggleStatus(String id, String ownerId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new SecurityException("User is not the owner of this task");
        }

        if (task.getStatus() == TaskStatus.PENDING) {
            task.setStatus(TaskStatus.COMPLETED);
        } else {
            task.setStatus(TaskStatus.PENDING);
        }

        taskRepository.save(task);
    }

    public void delete(String id, String ownerId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new SecurityException("User is not the owner of this task");
        }

        taskRepository.delete(task);
    }
}