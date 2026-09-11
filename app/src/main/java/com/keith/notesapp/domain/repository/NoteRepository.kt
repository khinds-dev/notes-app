package com.keith.notesapp.domain.repository

import com.keith.notesapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}
