package com.keith.notesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keith.notesapp.data.local.dao.NoteDao
import com.keith.notesapp.data.local.entity.NoteEntity

@Database(
    version = 1,
    entities = [NoteEntity::class],
    exportSchema = true
)
abstract class NotesDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val NAME = "notes_db"
    }
}
