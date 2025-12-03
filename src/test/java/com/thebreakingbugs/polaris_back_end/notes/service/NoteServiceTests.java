package com.thebreakingbugs.polaris_back_end.notes.service;

import com.thebreakingbugs.polaris_back_end.notes.dto.CreateNoteDTO;
import com.thebreakingbugs.polaris_back_end.notes.dto.UpdateNoteDTO;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        CreateNoteDTO input = new CreateNoteDTO("Note", "Content");
        Note saved = new Note("Note", "Content", "mod-1", "user-123");
        saved.setId("note-1");

        when(noteRepository.save(any(Note.class))).thenReturn(saved);

        // ACT
        Note result = noteService.create(input, "mod-1", "user-123");

        // ASSERT
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("Note", result.getTitle());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("[BR-001]")
    void shouldThrowExceptionWhenModuleIdIsMissing() {
        // ARRANGE
        CreateNoteDTO input = new CreateNoteDTO("Note", "Content");

        // ACT & ASSERT
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(input, null, "user-123");
        });

        Assertions.assertEquals("Module ID is required", exception.getMessage());
        verify(noteRepository, never()).save(any());
    }

    @Test
    @DisplayName("[BR-002]")
    void shouldThrowExceptionWhenTitleIsTooLong() {
        // ARRANGE
        String longTitle = "A".repeat(101);
        CreateNoteDTO input = new CreateNoteDTO(longTitle, "Content");

        // ACT & ASSERT
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(input, "mod-1", "user-123");
        });

        Assertions.assertEquals("Note title exceeds maximum length of 100 characters", exception.getMessage());
        verify(noteRepository, never()).save(any());
    }

    @Test
    @DisplayName("[FR-002]")
    void shouldListNotesByModule() {
        // ARRANGE
        when(noteRepository.findAllByModuleIdAndOwnerId("mod-1", "user-123"))
                .thenReturn(List.of(new Note(), new Note()));

        // ACT
        List<Note> result = noteService.findAllByModuleId("mod-1", "user-123");

        // ASSERT
        Assertions.assertEquals(2, result.size());
        verify(noteRepository, times(1)).findAllByModuleIdAndOwnerId("mod-1", "user-123");
    }

    @Test
    @DisplayName("[FR-003]")
    void shouldSearchNotesByTerm() {
        // ARRANGE
        String searchTerm = "Teorema";
        Note note = new Note("O Teorema de Pitágoras", "...", "mod-1", "user-123");
        when(noteRepository.searchByTermInModule("mod-1", searchTerm, "user-123"))
                .thenReturn(List.of(note));

        // ACT
        List<Note> result = noteService.search("mod-1", searchTerm, "user-123");

        // ASSERT
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
    }

    @Test
    @DisplayName("[FR-003]")
    void shouldReturnEmptyListWhenSearchTermIsNotFound() {
        // ARRANGE
        String searchTerm = "Inexistente";
        when(noteRepository.searchByTermInModule("mod-1", searchTerm, "user-123"))
                .thenReturn(List.of());

        // ACT
        List<Note> result = noteService.search("mod-1", searchTerm, "user-123");

        // ASSERT
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("[FR-004]")
    void shouldGetNoteDetails() {
        // ARRANGE
        Note note = new Note("Title", "Content", "mod-1", "user-123");
        when(noteRepository.findByIdAndModuleIdAndOwnerId("note-1", "mod-1", "user-123"))
                .thenReturn(Optional.of(note));

        // ACT
        Note result = noteService.getDetails("note-1", "mod-1", "user-123");

        // ASSERT
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Title", result.getTitle());
    }

    @Test
    @DisplayName("[FR-004]")
    void shouldThrowExceptionWhenGettingDetailsOfNonExistentNote() {
        // ARRANGE
        when(noteRepository.findByIdAndModuleIdAndOwnerId(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        Assertions.assertThrows(RuntimeException.class, () -> {
            noteService.getDetails("note-non-existent", "mod-1", "user-123");
        });
    }

    @Test
    @DisplayName("[FR-005]")
    void shouldUpdateNote() {
        // ARRANGE
        UpdateNoteDTO changes = new UpdateNoteDTO("New Title", "New Content");
        Note existingNote = new Note("Old Title", "Old Content", "mod-1", "user-123");

        when(noteRepository.findByIdAndModuleIdAndOwnerId("note-1", "mod-1", "user-123"))
                .thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(i -> i.getArgument(0));

        // ACT
        Note result = noteService.update("note-1", "mod-1", changes, "user-123");

        // ASSERT
        Assertions.assertEquals("New Title", result.getTitle());
        Assertions.assertEquals("New Content", result.getContent());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    @DisplayName("[FR-005]")
    void shouldThrowExceptionWhenUpdatingNonExistentNote() {
        // ARRANGE
        UpdateNoteDTO changes = new UpdateNoteDTO("New Title", "New Content");
        when(noteRepository.findByIdAndModuleIdAndOwnerId(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        Assertions.assertThrows(RuntimeException.class, () -> {
            noteService.update("note-non-existent", "mod-1", changes, "user-123");
        });
    }

    @Test
    @DisplayName("[FR-006]")
    void shouldDeleteNote() {
        // ARRANGE
        Note note = new Note();
        when(noteRepository.findByIdAndModuleIdAndOwnerId("note-1", "mod-1", "user-123"))
                .thenReturn(Optional.of(note));
        doNothing().when(noteRepository).delete(note);

        // ACT
        noteService.delete("note-1", "mod-1", "user-123");

        // ASSERT
        verify(noteRepository, times(1)).delete(note);
    }

    @Test
    @DisplayName("[FR-006]")
    void shouldThrowExceptionWhenDeletingNonExistentNote() {
        // ARRANGE
        when(noteRepository.findByIdAndModuleIdAndOwnerId(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        Assertions.assertThrows(RuntimeException.class, () -> {
            noteService.delete("note-non-existent", "mod-1", "user-123");
        });
    }
}
