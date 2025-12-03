package com.thebreakingbugs.polaris_back_end.tasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebreakingbugs.polaris_back_end.tasks.dto.CreateTaskDTO;
import com.thebreakingbugs.polaris_back_end.tasks.dto.UpdateTaskDTO;
import com.thebreakingbugs.polaris_back_end.tasks.model.Task;
import com.thebreakingbugs.polaris_back_end.tasks.model.TaskStatus;
import com.thebreakingbugs.polaris_back_end.tasks.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private TaskService taskService;

    final String OWNER_ID = "user-123";
    final String TASK_ID = "task-1";
    final String MODULE_ID = "mod-1";

    @Test
    @DisplayName("[FR-001] POST /tasks")
    void shouldCreateTask() throws Exception {
        CreateTaskDTO request = new CreateTaskDTO(
            "Prova Final", "Matéria toda", LocalDateTime.now().plusDays(10), MODULE_ID
        );

        Task createdTask = new Task();
        createdTask.setId(TASK_ID);
        createdTask.setTitle(request.title());
        
        when(taskService.create(any(Task.class))).thenReturn(createdTask);

        mockMvc.perform(post("/tasks")
                .header("X-User-Id", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TASK_ID));
    }

    @Test
    @DisplayName("[FR-002] GET /tasks?status=PENDING")
    void shouldListPendingTasks() throws Exception {
        when(taskService.listByStatus(OWNER_ID, TaskStatus.PENDING))
            .thenReturn(List.of(new Task(), new Task()));

        mockMvc.perform(get("/tasks")
                .header("X-User-Id", OWNER_ID)
                .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("[FR-003] GET /tasks?status=COMPLETED")
    void shouldListCompletedTasks() throws Exception {
        when(taskService.listByStatus(OWNER_ID, TaskStatus.COMPLETED))
            .thenReturn(List.of(new Task()));

        mockMvc.perform(get("/tasks")
                .header("X-User-Id", OWNER_ID)
                .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("[FR-004] PUT /tasks/{id}")
    void shouldUpdateTask() throws Exception {
        UpdateTaskDTO request = new UpdateTaskDTO("New Title", null, null);
        
        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        when(taskService.update(eq(TASK_ID), any(UpdateTaskDTO.class), eq(OWNER_ID)))
            .thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/{id}", TASK_ID)
                .header("X-User-Id", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    @DisplayName("[FR-005] PATCH /tasks/{id}/status")
    void shouldToggleStatus() throws Exception {

        mockMvc.perform(patch("/tasks/{id}/status", TASK_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk());

        verify(taskService).toggleStatus(TASK_ID, OWNER_ID);
    }

    @Test
    @DisplayName("[FR-006] DELETE /tasks/{id}")
    void shouldDeleteTask() throws Exception {

        mockMvc.perform(delete("/tasks/{id}", TASK_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isNoContent());

        verify(taskService).delete(TASK_ID, OWNER_ID);
    }

    @Test
    @DisplayName("[FR-007] GET /tasks/{id}")
    void shouldGetTaskDetails() throws Exception {
        Task task = new Task();
        task.setId(TASK_ID);

        when(taskService.getDetails(TASK_ID, OWNER_ID)).thenReturn(task);

        mockMvc.perform(get("/tasks/{id}", TASK_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TASK_ID));
    }
}