package com.thebreakingbugs.polaris_back_end.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebreakingbugs.polaris_back_end.notes.dto.CreateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.dto.UpdateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private NoteService noteService;

    final String OWNER_ID = "user-123";
    final String MODULE_ID = "mod-1";
    final String NOTE_ID = "note-1";

    @Test
    @DisplayName("[FR-001] POST /modules/{moduleId}/notes")
    void shouldCreateNoteSuccessfully() throws Exception {
        CreateNoteDTO request = new CreateNoteDTO("Title", "Content");
        Note note = new Note("Title", "Content", MODULE_ID, OWNER_ID);
        note.setId(NOTE_ID);

        when(noteService.create(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/modules/{moduleId}/notes", MODULE_ID)
                        .header("X-User-Id", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(NOTE_ID));
    }

    @Test
    @DisplayName("[FR-002] GET /modules/{moduleId}/notes")
    void shouldListNotes() throws Exception {
        when(noteService.findAllByModuleId(MODULE_ID, OWNER_ID)).thenReturn(List.of(new Note()));

        mockMvc.perform(get("/modules/{moduleId}/notes", MODULE_ID)
                        .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(noteService).findAllByModuleId(MODULE_ID, OWNER_ID);
    }

    @Test
    @DisplayName("[FR-003] GET /modules/{moduleId}/notes/search - Keyword Matching")
    void shouldReturnOnlyMatchingNotesWhenSearching() throws Exception {
        Note matchNote = new Note("Teorema de Pitágoras", "Conteúdo...", MODULE_ID, OWNER_ID);
        matchNote.setId("note-pitagoras");

        when(noteService.search(MODULE_ID, "Pitágoras", OWNER_ID))
                .thenReturn(List.of(matchNote));

        mockMvc.perform(get("/modules/{moduleId}/notes/search", MODULE_ID)
                        .param("q", "Pitágoras")
                        .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Teorema de Pitágoras"))
                .andExpect(jsonPath("$.length()").value(1));
        
        verify(noteService).search(MODULE_ID, "Pitágoras", OWNER_ID);
    }

    @Test
    @DisplayName("[FR-003] GET /modules/{moduleId}/notes/search - Empty Results")
    void shouldReturnEmptyListForNonExistentTerm() throws Exception {
        when(noteService.search(MODULE_ID, "Inexistente", OWNER_ID))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/modules/{moduleId}/notes/search", MODULE_ID)
                        .param("q", "Inexistente")
                        .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
        
        verify(noteService).search(MODULE_ID, "Inexistente", OWNER_ID);
    }

    @Test
    @DisplayName("[FR-004] GET /modules/{moduleId}/notes/{noteId}")
    void shouldGetNoteDetails() throws Exception {
        Note note = new Note();
        note.setId(NOTE_ID);
        when(noteService.getDetails(NOTE_ID, MODULE_ID, OWNER_ID)).thenReturn(note);

        mockMvc.perform(get("/modules/{moduleId}/notes/{noteId}", MODULE_ID, NOTE_ID)
                        .header("X-User-Id", OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(NOTE_ID));

        verify(noteService).getDetails(NOTE_ID, MODULE_ID, OWNER_ID);
    }

    @Test
    @DisplayName("[FR-005] PUT /modules/{moduleId}/notes/{noteId}")
    void shouldUpdateNote() throws Exception {
        UpdateNoteDTO request = new UpdateNoteDTO("New Title", null);
        Note updated = new Note();
        updated.setTitle("New Title");

        when(noteService.update(eq(NOTE_ID), eq(MODULE_ID), any(UpdateNoteDTO.class), eq(OWNER_ID)))
                .thenReturn(updated);

        mockMvc.perform(put("/modules/{moduleId}/notes/{noteId}", MODULE_ID, NOTE_ID)
                        .header("X-User-Id", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));

        verify(noteService).update(eq(NOTE_ID), eq(MODULE_ID), any(UpdateNoteDTO.class), eq(OWNER_ID));
    }

    @Test
    @DisplayName("[FR-006] DELETE /modules/{moduleId}/notes/{noteId}")
    void shouldDeleteNote() throws Exception {
        mockMvc.perform(delete("/modules/{moduleId}/notes/{noteId}", MODULE_ID, NOTE_ID)
                        .header("X-User-Id", OWNER_ID))
                .andExpect(status().isNoContent());

        verify(noteService).delete(NOTE_ID, MODULE_ID, OWNER_ID);
    }
}
