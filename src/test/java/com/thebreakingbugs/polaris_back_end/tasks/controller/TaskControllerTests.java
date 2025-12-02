package com.thebreakingbugs.polaris_back_end.tasks.service;

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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("[FR-001] POST /tasks ")
    void shouldCreateTask() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
            "Prova Final", "Matéria toda", LocalDateTime.now().plusDays(10), MODULE_ID
        );

        when(taskService.create(any(Task.class)))
            .thenThrow(new UnsupportedOperationException("Not implemented yet"));

        mockMvc.perform(post("/tasks")
                .header("X-User-Id", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()); 
    }

    @Test
    @DisplayName("[FR-002] GET /tasks?status=PENDING")
    void shouldListPendingTasks() throws Exception {
        when(taskService.listByStatus(OWNER_ID, TaskStatus.PENDING))
            .thenThrow(new UnsupportedOperationException("Not implemented yet"));

        mockMvc.perform(get("/tasks")
                .header("X-User-Id", OWNER_ID)
                .param("status", "PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[FR-003] GET /tasks?status=COMPLETED")
    void shouldListCompletedTasks() throws Exception {
        when(taskService.listByStatus(OWNER_ID, TaskStatus.COMPLETED))
            .thenThrow(new UnsupportedOperationException("Not implemented yet"));

        mockMvc.perform(get("/tasks")
                .header("X-User-Id", OWNER_ID)
                .param("status", "COMPLETED"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[FR-004] PUT /tasks/{id}")
    void shouldUpdateTask() throws Exception {
        UpdateTaskRequest request = new UpdateTaskRequest("New Title", null, null);

        when(taskService.update(eq(TASK_ID), any(UpdateTaskRequest.class), eq(OWNER_ID)))
            .thenThrow(new UnsupportedOperationException("Not implemented yet"));

        mockMvc.perform(put("/tasks/{id}", TASK_ID)
                .header("X-User-Id", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("[FR-005] PATCH /tasks/{id}/status")
    void shouldToggleStatus() throws Exception {

        doThrow(new UnsupportedOperationException("Not implemented yet"))
            .when(taskService).toggleStatus(TASK_ID, OWNER_ID);

        mockMvc.perform(patch("/tasks/{id}/status", TASK_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("[FR-006] DELETE /tasks/{id}")
    void shouldDeleteTask() throws Exception {
        doThrow(new UnsupportedOperationException("Not implemented yet"))
            .when(taskService).delete(TASK_ID, OWNER_ID);

        mockMvc.perform(delete("/tasks/{id}", TASK_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isNoContent()); 

    }

    @Test
    @DisplayName("[FR-007] GET /tasks/{id}")
    void shouldGetTaskDetails() throws Exception {
        when(taskService.getDetails(TASK_ID, OWNER_ID))
            .thenThrow(new UnsupportedOperationException("Not implemented yet"));

        mockMvc.perform(get("/tasks/{id}", TASK_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk());
    }
}