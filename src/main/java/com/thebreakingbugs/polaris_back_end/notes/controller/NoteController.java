package com.thebreakingbugs.polaris_back_end.notes.controller;

import com.thebreakingbugs.polaris_back_end.notes.dto.CreateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.dto.UpdateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modules/{moduleId}/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> create(
            @PathVariable String moduleId,
            @RequestBody @Valid CreateNoteDTO createNoteDTO,
            @RequestHeader("X-User-Id") String ownerId
    ) {
        Note createdNote = noteService.create(createNoteDTO, moduleId, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @GetMapping
    public ResponseEntity<List<Note>> list(
            @PathVariable String moduleId,
            @RequestHeader("X-User-Id") String ownerId
    ) {
        List<Note> notes = noteService.findAllByModuleId(moduleId, ownerId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Note>> search(
            @PathVariable String moduleId,
            @RequestParam("q") String query,
            @RequestHeader("X-User-Id") String ownerId
    ) {
        List<Note> notes = noteService.search(moduleId, query, ownerId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Note> getDetails(
            @PathVariable String moduleId,
            @PathVariable String noteId,
            @RequestHeader("X-User-Id") String ownerId
    ) {
        Note note = noteService.getDetails(noteId, moduleId, ownerId);
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Note> update(
            @PathVariable String moduleId,
            @PathVariable String noteId,
            @RequestBody @Valid UpdateNoteDTO updateNoteDTO,
            @RequestHeader("X-User-Id") String ownerId
    ) {
        Note updatedNote = noteService.update(noteId, moduleId, updateNoteDTO, ownerId);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> delete(
            @PathVariable String moduleId,
            @PathVariable String noteId,
            @RequestHeader("X-User-Id") String ownerId
    ) {
        noteService.delete(noteId, moduleId, ownerId);
        return ResponseEntity.noContent().build();
    }
}
