package com.thebreakingbugs.polaris_back_end.modules.service;

import java.util.List;
import java.util.NoSuchElementException;

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
        if (moduleRepository.existsByNameAndOwnerId(module.getName(), module.getOwnerId())) {
            throw new IllegalArgumentException("Module name already exists for this user");
        }
        return moduleRepository.save(module);
    }

    
    public List<Module> listActive(String ownerId) {
        return moduleRepository.findByOwnerIdAndArchivedFalse(ownerId);
    }

    
    public Module getDetails(String id, String ownerId) {
        return findOwnedModuleOrThrow(id, ownerId);
    }

    
    public Module update(String id, UpdateModuleDTO request, String ownerId) {
        Module module = findOwnedModuleOrThrow(id, ownerId);

        if (request.name() != null) module.setName(request.name());
        if (request.description() != null) module.setDescription(request.description());
        if (request.color() != null) module.setColor(request.color());

        return moduleRepository.save(module);
    }

    
    public void toggleArchive(String id, String ownerId) {
        Module module = findOwnedModuleOrThrow(id, ownerId);
        
        boolean isArchived = Boolean.TRUE.equals(module.getArchived());
        module.setArchived(!isArchived);
        
        moduleRepository.save(module);
    }

    
    public void delete(String id, String ownerId) {
        Module module = findOwnedModuleOrThrow(id, ownerId);
        moduleRepository.delete(module);
    }

    private Module findOwnedModuleOrThrow(String id, String ownerId) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Module not found"));

        if (!module.getOwnerId().equals(ownerId)) {
            throw new SecurityException("User is not the owner of this module");
        }
        return module;
    }
}
