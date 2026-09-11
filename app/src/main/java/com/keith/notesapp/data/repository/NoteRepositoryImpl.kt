package com.keith.notesapp.data.repository

import com.keith.notesapp.data.local.dao.NoteDao
import com.keith.notesapp.data.local.entity.NoteEntity
import com.keith.notesapp.domain.model.Note
import com.keith.notesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        dao.getAllNotes().map { entities -> entities.map { it.toDomain() } }

    override fun searchNotes(query: String): Flow<List<Note>> =
        dao.searchNotes(query).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getNoteById(id: Long): Note? =
        dao.getNoteById(id)?.toDomain()

    override suspend fun insertNote(note: Note): Long =
        dao.insertNote(note.toEntity())

    override suspend fun updateNote(note: Note) =
        dao.updateNote(note.toEntity())

    override suspend fun deleteNote(note: Note) =
        dao.deleteNote(note.toEntity())

    private fun NoteEntity.toDomain() = Note(id = id, title = title, content = content, timestamp = timestamp)
    private fun Note.toEntity() = NoteEntity(id = id, title = title, content = content, timestamp = timestamp)
}
