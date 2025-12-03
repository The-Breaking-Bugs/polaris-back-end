package com.thebreakingbugs.polaris_back_end.notes.service;

import com.thebreakingbugs.polaris_back_end.notes.dto.CreateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.dto.UpdateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private static final int MAX_TITLE_LENGTH = 100;

    private final NoteRepository noteRepository ;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note create(CreateNoteDTO createNoteDTO, String moduleId, String ownerId) {
        if (moduleId == null || moduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Module ID is required");
        }
        if (createNoteDTO.title() != null && createNoteDTO.title().length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Note title exceeds maximum length of " + MAX_TITLE_LENGTH + " characters");
        }

        Note note = new Note(
            createNoteDTO.title(),
            createNoteDTO.content(),
            moduleId,
            ownerId
        );

        return noteRepository.save(note);
    }

    public List<Note> findAllByModuleId(String moduleId, String ownerId) {
        return noteRepository.findAllByModuleIdAndOwnerId(moduleId, ownerId);
    }

    public Note getDetails(String noteId, String moduleId, String ownerId) {
        return noteRepository.findByIdAndModuleIdAndOwnerId(noteId, moduleId, ownerId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public Note update(String noteId, String moduleId, UpdateNoteDTO changes, String ownerId) {
        Note note = getDetails(noteId, moduleId, ownerId);

        if (changes.title() != null) {
            note.setTitle(changes.title());
        }
        if (changes.content() != null) {
            note.setContent(changes.content());
        }

        return noteRepository.save(note);
    }

    public void delete(String noteId, String moduleId, String ownerId) {
        Note note = getDetails(noteId, moduleId, ownerId);
        noteRepository.delete(note);
    }

    public List<Note> search(String moduleId, String searchTerm, String ownerId) {
        return noteRepository.searchByTermInModule(moduleId, searchTerm, ownerId);
    }
}