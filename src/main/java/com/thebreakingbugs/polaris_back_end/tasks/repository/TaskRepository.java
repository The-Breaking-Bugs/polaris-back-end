package com.thebreakingbugs.polaris_back_end.tasks.repository;

import com.thebreakingbugs.polaris_back_end.tasks.model.Task;
import com.thebreakingbugs.polaris_back_end.tasks.model.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    
    List<Task> findByOwnerIdAndStatusOrderByDueDateAsc(String ownerId, TaskStatus status);
    
    List<Task> findByOwnerIdAndStatusOrderByDueDateDesc(String ownerId, TaskStatus status);
}
