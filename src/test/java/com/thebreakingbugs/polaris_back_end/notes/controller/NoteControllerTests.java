package com.thebreakingbugs.polaris_back_end.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebreakingbugs.polaris_back_end.notes.dto.CreateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
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
}
