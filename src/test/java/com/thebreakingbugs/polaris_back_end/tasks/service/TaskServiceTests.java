package com.thebreakingbugs.polaris_back_end.tasks.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import com.thebreakingbugs.polaris_back_end.modules.repository.ModuleRepository;
import com.thebreakingbugs.polaris_back_end.tasks.model.Task;
import com.thebreakingbugs.polaris_back_end.tasks.model.TaskStatus;
import com.thebreakingbugs.polaris_back_end.tasks.repository.TaskRepository;
import com.thebreakingbugs.polaris_back_end.tasks.dto.UpdateTaskDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTests {

    @Mock private TaskRepository taskRepository;
    @Mock private ModuleRepository moduleRepository;
    @InjectMocks private TaskService taskService;

    final String OWNER_ID = "user-123";
    final String OTHER_USER = "user-999";
    final String MODULE_ID = "mod-1";
    final String TASK_ID = "task-1";

    @Test
    @DisplayName("[FR-001]")
    void shouldCreateTask() {
        Task task = new Task("Prova 1", "Calculo", LocalDateTime.now().plusDays(5), MODULE_ID, OWNER_ID);
        
        Module module = new Module();
        module.setId(MODULE_ID);
        module.setOwnerId(OWNER_ID);
        module.setArchived(false);

        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task created = taskService.create(task);

        assertNotNull(created);
        assertEquals(TaskStatus.PENDING, created.getStatus()); 
    }

    @Test
    @DisplayName("[BR-003]")
    void shouldThrowIfModuleArchived() {
        Task task = new Task("Task", "Desc", LocalDateTime.now().plusDays(1), MODULE_ID, OWNER_ID);
        
        Module archivedModule = new Module();
        archivedModule.setId(MODULE_ID);
        archivedModule.setOwnerId(OWNER_ID); 
        archivedModule.setArchived(true);

        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(archivedModule));


        assertThrows(IllegalArgumentException.class, () -> taskService.create(task));
    }

    @Test
    @DisplayName("[BR-002]")
    void shouldThrowIfModuleNotFound() {
        Task task = new Task("Task", "Desc", LocalDateTime.now().plusDays(1), "invalid-id", OWNER_ID);
        when(moduleRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.create(task)); 
    }

    @Test
    @DisplayName("[FR-002]")
    void shouldndingListPeOrdered() {
        when(taskRepository.findByOwnerIdAndStatusOrderByDueDateAsc(OWNER_ID, TaskStatus.PENDING))
            .thenReturn(List.of(new Task()));

        List<Task> result = taskService.listByStatus(OWNER_ID, TaskStatus.PENDING);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(taskRepository).findByOwnerIdAndStatusOrderByDueDateAsc(OWNER_ID, TaskStatus.PENDING);
    }

    @Test
    @DisplayName("[FR-003]")
    void shouldListHistoryOrdered() {
        when(taskRepository.findByOwnerIdAndStatusOrderByDueDateDesc(OWNER_ID, TaskStatus.COMPLETED))
            .thenReturn(List.of(new Task()));

        List<Task> result = taskService.listByStatus(OWNER_ID, TaskStatus.COMPLETED);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(taskRepository).findByOwnerIdAndStatusOrderByDueDateDesc(OWNER_ID, TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("[BR-001] e [FR-007]")
    void shouldGetDetails() {
        Task task = new Task();
        task.setOwnerId(OWNER_ID);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

        Task result = taskService.getDetails(TASK_ID, OWNER_ID);
        assertNotNull(result);
        assertEquals(OWNER_ID, result.getOwnerId());
    }

    @Test
    @DisplayName("[FR-004]")
    void shouldUpdateTask() {
        UpdateTaskDTO request = new UpdateTaskDTO("New Title", null, null);
        Task existing = new Task();
        existing.setOwnerId(OWNER_ID);
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(existing);

        Task updated = taskService.update(TASK_ID, request, OWNER_ID);

        assertEquals("New Title", updated.getTitle());
    }

    @Test
    @DisplayName("[FR-005]")
    void shouldToggleStatus() {
        Task task = new Task();
        task.setOwnerId(OWNER_ID);
        task.setStatus(TaskStatus.PENDING); 

        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

        taskService.toggleStatus(TASK_ID, OWNER_ID);

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("[FR-006]")
    void shouldDeleteTask() {
        Task task = new Task();
        task.setOwnerId(OWNER_ID);
        
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

        taskService.delete(TASK_ID, OWNER_ID);

        verify(taskRepository).delete(task);
    }
}