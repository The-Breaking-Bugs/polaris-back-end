package com.thebreakingbugs.polaris_back_end.modules.service;

import com.thebreakingbugs.polaris_back_end.modules.dto.UpdateModuleDTO;
import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import com.thebreakingbugs.polaris_back_end.modules.repository.ModuleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleServiceTests {

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleService moduleService;

    final String OWNER_ID = "user-123";
    final String OTHER_USER = "user-999";
    final String MODULE_ID = "mod-1";


    @Test
    @DisplayName("[FR-001]")
    void shouldCreateModuleSuccess() {
        Module module = new Module("Math", "Desc", "#000", OWNER_ID);
        
        when(moduleRepository.existsByNameAndOwnerId("Math", OWNER_ID)).thenReturn(false);
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        Module created = moduleService.create(module);

        assertNotNull(created, "Service returned null but should return the created module");
        verify(moduleRepository).save(module);
    }

    @Test
    @DisplayName("[BR-002]")
    void shouldThrowExceptionWhenDuplicateName() {
        
        Module module = new Module("Math", "Desc", "#000", OWNER_ID);
        when(moduleRepository.existsByNameAndOwnerId("Math", OWNER_ID)).thenReturn(true);

        
        assertThrows(IllegalArgumentException.class, () -> moduleService.create(module));

        verify(moduleRepository, never()).save(any());
    }

    @Test
    @DisplayName("[FR-002]")
    void shouldListActiveModules() {

        when(moduleRepository.findByOwnerIdAndArchivedFalse(OWNER_ID))
                .thenReturn(List.of(new Module("Math", "Desc", "#000", OWNER_ID)));

        List<Module> result = moduleService.listActive(OWNER_ID);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(moduleRepository).findByOwnerIdAndArchivedFalse(OWNER_ID);
    }

    @Test
    @DisplayName("[FR-003]")
    void shouldGetDetailsSuccess() {

        Module module = new Module("Math", "Desc", "#000", OWNER_ID);
        module.setId(MODULE_ID);
        
        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module));

        Module result = moduleService.getDetails(MODULE_ID, OWNER_ID);

        assertNotNull(result);
        assertEquals(MODULE_ID, result.getId());
    }

    @Test
    @DisplayName("[BR-001]")
    void shouldThrowForbiddenWhenOwnerMismatch() {
        
        Module module = new Module("Math", "Desc", "#000", OTHER_USER); // Dono diferente
        module.setId(MODULE_ID);
        
        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module));

        
        assertThrows(SecurityException.class, () -> moduleService.getDetails(MODULE_ID, OWNER_ID));
    }

    @Test
    @DisplayName("[FR-004]")
    void shouldUpdateModule() {
        
        UpdateModuleDTO request = new UpdateModuleDTO("New Name", "New Desc", null);
        Module existing = new Module("Old Name", "Old Desc", "#000", OWNER_ID);
        existing.setId(MODULE_ID);

        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(existing));
        when(moduleRepository.save(any(Module.class))).thenAnswer(i -> i.getArguments()[0]); // Retorna o que foi salvo

        
        Module updated = moduleService.update(MODULE_ID, request, OWNER_ID);

        
        assertNotNull(updated);
        assertEquals("New Name", updated.getName());
        assertEquals("New Desc", updated.getDescription());
        assertEquals("#000", updated.getColor()); // Não mudou
    }

    @Test
    @DisplayName("[FR-005]")
    void shouldArchiveModule() {
        
        Module existing = new Module("Math", "Desc", "#000", OWNER_ID);
        existing.setId(MODULE_ID);
        existing.setArchived(false); // Começa ativo

        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(existing));

        
        moduleService.toggleArchive(MODULE_ID, OWNER_ID);

        
        assertTrue(existing.getArchived(), "Module should be archived now");
        verify(moduleRepository).save(existing);
    }

    @Test
    @DisplayName("[FR-006]")
    void shouldDeleteModule() {
        
        Module existing = new Module("Math", "Desc", "#000", OWNER_ID);
        existing.setId(MODULE_ID);

        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(existing));

        
        moduleService.delete(MODULE_ID, OWNER_ID);

        
        verify(moduleRepository).delete(existing);
    }
    
}
