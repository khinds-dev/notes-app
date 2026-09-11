package com.keith.notesapp.domain.usecase

import com.keith.notesapp.domain.model.Note
import com.keith.notesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}

class SearchNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(query: String): Flow<List<Note>> = repository.searchNotes(query)
}

class GetNoteByIdUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Long): Note? = repository.getNoteById(id)
}

class InsertNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note): Long = repository.insertNote(note)
}

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val searchNotes: SearchNotesUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val insertNote: InsertNoteUseCase,
    val updateNote: UpdateNoteUseCase,
    val deleteNote: DeleteNoteUseCase
)
