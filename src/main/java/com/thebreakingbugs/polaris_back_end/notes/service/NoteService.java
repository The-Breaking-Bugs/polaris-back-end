package com.thebreakingbugs.polaris_back_end.notes.service;

import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private static final int MAX_TITLE_LENGTH = 100;

    private final NoteRepository noteRepository ;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note create(Note note) {
        if (note.getModuleId() == null || note.getModuleId().trim().isEmpty()) {
            throw new IllegalArgumentException("Module ID is required");
        }

        if (note.getTitle() != null && note.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Note title exceeds maximum length of " + MAX_TITLE_LENGTH + " characters");
        }

        return noteRepository.save(note);
    }
}