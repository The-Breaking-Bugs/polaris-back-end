package com.thebreakingbugs.polaris_back_end.notes.repository;

import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {

}
