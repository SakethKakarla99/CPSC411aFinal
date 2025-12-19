package com.example.cpsc411afinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsc411afinal.data.local.NoteEntity
import com.example.cpsc411afinal.data.repo.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

enum class NoteSort {
    TITLE_AZ, TITLE_ZA, DATE_NEWEST, DATE_OLDEST
}

class NotesListViewModel(
    private val repo: NotesRepository,
    private val folderId: Long
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _sort = MutableStateFlow(NoteSort.DATE_NEWEST)
    val sort: StateFlow<NoteSort> = _sort

    private val baseNotes: Flow<List<NoteEntity>> = repo.notes(folderId)

    val visibleNotes: StateFlow<List<NoteEntity>> =
        combine(baseNotes, _query, _sort) { notes, q, sort ->
            val queryLower = q.trim().lowercase(Locale.getDefault())

            val filtered = if (queryLower.isEmpty()) notes else notes.filter { n ->
                n.title.lowercase(Locale.getDefault()).contains(queryLower) ||
                        n.content.lowercase(Locale.getDefault()).contains(queryLower)
            }

            when (sort) {
                NoteSort.TITLE_AZ -> filtered.sortedBy { it.title.lowercase(Locale.getDefault()) }
                NoteSort.TITLE_ZA -> filtered.sortedByDescending { it.title.lowercase(Locale.getDefault()) }
                NoteSort.DATE_NEWEST -> filtered.sortedByDescending { it.updatedAt }
                NoteSort.DATE_OLDEST -> filtered.sortedBy { it.updatedAt }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(v: String) { _query.value = v }
    fun setSort(v: NoteSort) { _sort.value = v }

    fun delete(note: NoteEntity) {
        viewModelScope.launch { repo.deleteNote(note) }
    }
}



