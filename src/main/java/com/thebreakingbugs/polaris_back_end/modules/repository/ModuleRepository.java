package com.thebreakingbugs.polaris_back_end.modules.repository;

import com.thebreakingbugs.polaris_back_end.modules.model.Module;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModuleRepository extends MongoRepository<Module, String> {
}
