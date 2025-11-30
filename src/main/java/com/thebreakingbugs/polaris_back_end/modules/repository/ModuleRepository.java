package com.thebreakingbugs.polaris_back_end.modules.repository;

import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends MongoRepository<Module, String> {

    boolean existsByNameAndOwnerId(String name, String ownerId);

    List<Module> findByOwnerIdAndArchivedFalse(String ownerId);

    Optional<Module> findByIdAndOwnerId(String id, String ownerId);
}
