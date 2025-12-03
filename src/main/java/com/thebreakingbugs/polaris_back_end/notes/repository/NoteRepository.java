package com.thebreakingbugs.polaris_back_end.notes.repository;

import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findAllByModuleIdAndOwnerId(String moduleId, String ownerId);

    Optional<Note> findByIdAndModuleIdAndOwnerId(String id, String moduleId, String ownerId);

    @Query("{$and: [ { 'moduleId': ?0, 'ownerId': ?2 }, { $text: { $search: ?1 } } ] }")
    List<Note> searchByTermInModule(String moduleId, String searchTerm, String ownerId);
}
