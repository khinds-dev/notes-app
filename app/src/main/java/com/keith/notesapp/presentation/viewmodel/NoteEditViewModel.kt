package com.keith.notesapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keith.notesapp.domain.model.Note
import com.keith.notesapp.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val useCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Long = savedStateHandle.get<Long>("noteId") ?: -1L

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private var originalNote: Note? = null

    init {
        if (noteId != -1L) {
            viewModelScope.launch {
                useCases.getNoteById(noteId)?.let { note ->
                    originalNote = note
                    _title.value = note.title
                    _content.value = note.content
                }
            }
        }
    }

    fun onTitleChange(value: String) { _title.update { value } }
    fun onContentChange(value: String) { _content.update { value } }

    fun saveNote(onSaved: () -> Unit) {
        viewModelScope.launch {
            val trimmedTitle = _title.value.trim()
            val trimmedContent = _content.value.trim()
            if (trimmedTitle.isBlank() && trimmedContent.isBlank()) {
                onSaved()
                return@launch
            }
            if (originalNote != null) {
                useCases.updateNote(
                    originalNote!!.copy(
                        title = trimmedTitle,
                        content = trimmedContent,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } else {
                useCases.insertNote(
                    Note(
                        title = trimmedTitle,
                        content = trimmedContent,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
            onSaved()
        }
    }
}
