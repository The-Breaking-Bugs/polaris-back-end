package com.thebreakingbugs.polaris_back_end.notes.service;

import com.thebreakingbugs.polaris_back_end.notes.model.Note;
import com.thebreakingbugs.polaris_back_end.notes.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTests {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    @DisplayName("[FR-001]")
    public void shouldCreateNoteSuccessfully() {
        // ARRANGE
        Note input = new Note("Note", "Content", "mod-1", "user-123");
        Note saved = new Note("Note", "Content", "mod-1", "user-123");
        saved.setId("note-1");
        Mockito.when(noteRepository.save(any(Note.class))).thenReturn(saved);

        // ACT
        Note result = noteService.create(input);

        // ASSERT
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("Note", result.getTitle());
        Mockito.verify(noteRepository, Mockito.times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("[BR-001]")
    void shouldThrowExceptionWhenModuleIdIsMissing() {
        // ARRANGE
        Note input = new Note("Note", "Content", null, "user-123");

        // ACT
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(input);
        });

        // ASSERT
        Assertions.assertEquals("Module ID is required", exception.getMessage());
    }

    @Test
    @DisplayName("[BR-002]")
    void shouldThrowExceptionWhenTitleIsTooLong() {
        // ARRANGE
        String longTitle = "A".repeat(101);
        Note input = new Note(longTitle, "Content", "mod-1", "user-123");

        // ACT
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(input);
        });
        
        // ASSERT
        Assertions.assertEquals("Title cannot exceed 100 characters", exception.getMessage());
    }
}