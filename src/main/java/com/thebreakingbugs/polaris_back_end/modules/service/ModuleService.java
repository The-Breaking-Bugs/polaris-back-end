package com.thebreakingbugs.polaris_back_end.modules.service;

import java.util.List;
import com.thebreakingbugs.polaris_back_end.modules.repository.ModuleRepository;
import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import com.thebreakingbugs.polaris_back_end.modules.dto.UpdateModuleDTO;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Module create(Module module) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    
    public List<Module> listActive(String ownerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    
    public Module getDetails(String id, String ownerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    
    public Module update(String id, UpdateModuleDTO request, String ownerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    
    public void toggleArchive(String id, String ownerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    
    public void delete(String id, String ownerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
