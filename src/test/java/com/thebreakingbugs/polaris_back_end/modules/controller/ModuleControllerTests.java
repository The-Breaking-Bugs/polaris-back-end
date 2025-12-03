package com.thebreakingbugs.polaris_back_end.modules.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebreakingbugs.polaris_back_end.modules.dto.ModuleDTO;
import com.thebreakingbugs.polaris_back_end.modules.dto.UpdateModuleDTO;
import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import com.thebreakingbugs.polaris_back_end.modules.service.ModuleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModuleController.class)
public class ModuleControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private ModuleService moduleService;

    final String OWNER_ID = "user-123";
    final String MODULE_ID = "mod-1";

    @Test
    @DisplayName("[FR-001] POST /modules")
    void shouldCreate() throws Exception {
        ModuleDTO request = new ModuleDTO("Math", "Desc", "#000");
        Module module = new Module("Math", "Desc", "#000", OWNER_ID);
        module.setId(MODULE_ID);

        when(moduleService.create(any(Module.class))).thenReturn(module);

        mockMvc.perform(post("/modules")
                .header("X-User-Id", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(MODULE_ID));
    }

    @Test
    @DisplayName("[FR-002] GET /modules")
    void shouldListActive() throws Exception {
        when(moduleService.listActive(OWNER_ID)).thenReturn(List.of(new Module()));

        mockMvc.perform(get("/modules")
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("[FR-003] GET /modules/{id}")
    void shouldGetDetails() throws Exception {
        Module module = new Module();
        module.setId(MODULE_ID);
        when(moduleService.getDetails(MODULE_ID, OWNER_ID)).thenReturn(module);

        mockMvc.perform(get("/modules/{id}", MODULE_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MODULE_ID));
    }

    @Test
    @DisplayName("[FR-004] PUT /modules/{id}")
    void shouldUpdate() throws Exception {
        UpdateModuleDTO request = new UpdateModuleDTO("New Name", null, null);
        Module updated = new Module();
        updated.setName("New Name");
        
        when(moduleService.update(eq(MODULE_ID), any(UpdateModuleDTO.class), eq(OWNER_ID)))
                .thenReturn(updated);

        mockMvc.perform(put("/modules/{id}", MODULE_ID)
                .header("X-User-Id", OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    @DisplayName("[FR-005] PATCH /modules/{id}/archive")
    void shouldArchive() throws Exception {

        mockMvc.perform(patch("/modules/{id}/archive", MODULE_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk());

        verify(moduleService).toggleArchive(MODULE_ID, OWNER_ID);
    }

    @Test
    @DisplayName("[FR-006] DELETE /modules/{id}")
    void shouldDelete() throws Exception {
        
        mockMvc.perform(delete("/modules/{id}", MODULE_ID)
                .header("X-User-Id", OWNER_ID))
                .andExpect(status().isNoContent());

        verify(moduleService).delete(MODULE_ID, OWNER_ID);
    }
}
