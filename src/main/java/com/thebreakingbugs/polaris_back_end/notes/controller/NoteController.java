package com.thebreakingbugs.polaris_back_end.notes.controller;

import com.thebreakingbugs.polaris_back_end.notes.dto.CreateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        @RequestBody @Valid CreateNoteDTO request,
        @RequestHeader("X-User-Id") String ownerId
    ) {
        Note noteToCreate = new Note(
            request.title(),
            request.content(),
            moduleId,
            ownerId
        );
        Note createdNote = noteService.create(noteToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }
}
