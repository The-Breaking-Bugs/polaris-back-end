package com.thebreakingbugs.polaris_back_end.modules.controller;

import com.thebreakingbugs.polaris_back_end.modules.dto.ModuleDTO;
import com.thebreakingbugs.polaris_back_end.modules.dto.UpdateModuleDTO;
import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import com.thebreakingbugs.polaris_back_end.modules.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Module create(@RequestHeader("X-User-Id") String ownerId, 
                         @RequestBody @Valid ModuleDTO request) {
        Module newModule = new Module(request.name(), request.description(), request.color(), ownerId);
        return moduleService.create(newModule);
    }

    @GetMapping
    public List<Module> list(@RequestHeader("X-User-Id") String ownerId) {
        return moduleService.listActive(ownerId);
    }

    @GetMapping("/{id}")
    public Module getDetails(@RequestHeader("X-User-Id") String ownerId, 
                             @PathVariable String id) {
        return moduleService.getDetails(id, ownerId);
    }

    @PutMapping("/{id}")
    public Module update(@RequestHeader("X-User-Id") String ownerId, 
                         @PathVariable String id, 
                         @RequestBody @Valid UpdateModuleDTO request) {
        return moduleService.update(id, request, ownerId);
    }

    @PatchMapping("/{id}/archive")
    public void archive(@RequestHeader("X-User-Id") String ownerId, 
                        @PathVariable String id) {
        moduleService.toggleArchive(id, ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader("X-User-Id") String ownerId, 
                       @PathVariable String id) {
        moduleService.delete(id, ownerId);
    }
}
