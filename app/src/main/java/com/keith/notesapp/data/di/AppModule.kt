package com.keith.notesapp.data.di

import android.content.Context
import androidx.room.Room
import com.keith.notesapp.data.local.NotesDatabase
import com.keith.notesapp.data.repository.NoteRepositoryImpl
import com.keith.notesapp.domain.repository.NoteRepository
import com.keith.notesapp.domain.usecase.DeleteNoteUseCase
import com.keith.notesapp.domain.usecase.GetNoteByIdUseCase
import com.keith.notesapp.domain.usecase.GetNotesUseCase
import com.keith.notesapp.domain.usecase.InsertNoteUseCase
import com.keith.notesapp.domain.usecase.NoteUseCases
import com.keith.notesapp.domain.usecase.SearchNotesUseCase
import com.keith.notesapp.domain.usecase.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context): NotesDatabase =
        Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            NotesDatabase.NAME
        ).build()

    @Provides
    @Singleton
    fun provideNoteRepository(database: NotesDatabase): NoteRepository =
        NoteRepositoryImpl(dao = database.noteDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases = NoteUseCases(
        getNotes = GetNotesUseCase(repository),
        searchNotes = SearchNotesUseCase(repository),
        getNoteById = GetNoteByIdUseCase(repository),
        insertNote = InsertNoteUseCase(repository),
        updateNote = UpdateNoteUseCase(repository),
        deleteNote = DeleteNoteUseCase(repository)
    )
}
