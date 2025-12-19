package com.example.cpsc411afinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsc411afinal.data.local.FolderEntity
import com.example.cpsc411afinal.data.repo.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

enum class FolderSort {
    NAME_AZ, NAME_ZA
}

class FolderListViewModel(private val repo: NotesRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _sort = MutableStateFlow(FolderSort.NAME_AZ)
    val sort: StateFlow<FolderSort> = _sort

    private val baseFolders: Flow<List<FolderEntity>> = repo.folders()

    val visibleFolders: StateFlow<List<FolderEntity>> =
        combine(baseFolders, _query, _sort) { folders, q, sort ->
            val queryLower = q.trim().lowercase(Locale.getDefault())

            val filtered = if (queryLower.isEmpty()) folders else folders.filter { f ->
                f.name.lowercase(Locale.getDefault()).contains(queryLower)
            }

            when (sort) {
                FolderSort.NAME_AZ -> filtered.sortedBy { it.name.lowercase(Locale.getDefault()) }
                FolderSort.NAME_ZA -> filtered.sortedByDescending { it.name.lowercase(Locale.getDefault()) }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(v: String) { _query.value = v }
    fun setSort(v: FolderSort) { _sort.value = v }

    fun addFolder(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repo.addFolder(trimmed) }
    }

    fun deleteFolder(folder: FolderEntity) {
        viewModelScope.launch { repo.deleteFolder(folder) }
    }
}



