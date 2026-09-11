package com.keith.notesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keith.notesapp.domain.model.Note
import com.keith.notesapp.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val useCases: NoteUseCases
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val notes = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) useCases.getNotes()
            else useCases.searchNotes(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            useCases.deleteNote(note)
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            useCases.insertNote(note.copy(id = 0))
        }
    }
}
