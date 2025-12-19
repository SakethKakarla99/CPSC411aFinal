package com.example.cpsc411afinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsc411afinal.data.local.NoteEntity
import com.example.cpsc411afinal.data.repo.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteEditUiState(
    val title: String = "",
    val content: String = "",
    val titleError: String? = null,
    val saving: Boolean = false
)

class NoteEditViewModel(
    private val repo: NotesRepository,
    private val folderId: Long,
    private val noteId: Long? // ✅ null = new note, otherwise edit existing
) : ViewModel() {

    private var existing: NoteEntity? = null

    private val _ui = MutableStateFlow(NoteEditUiState())
    val ui: StateFlow<NoteEditUiState> = _ui

    init {
        // ✅ If editing, load the note from DB
        viewModelScope.launch {
            val id = noteId ?: return@launch
            existing = repo.getNoteById(id)
            existing?.let { n ->
                _ui.update { it.copy(title = n.title, content = n.content) }
            }
        }
    }

    fun setTitle(v: String) = _ui.update { it.copy(title = v, titleError = null) }
    fun setContent(v: String) = _ui.update { it.copy(content = v) }

    private fun validate(): Boolean {
        val t = ui.value.title.trim()
        return if (t.isEmpty()) {
            _ui.update { it.copy(titleError = "Title cannot be empty.") }
            false
        } else true
    }

    fun save(onDone: () -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            _ui.update { it.copy(saving = true) }

            val t = ui.value.title.trim()
            val c = ui.value.content

            val ex = existing
            if (ex == null) {
                // ✅ ADD
                repo.addNote(folderId = folderId, title = t, content = c)
            } else {
                // ✅ UPDATE
                repo.updateNote(ex.copy(title = t, content = c))
            }

            _ui.update { it.copy(saving = false) }
            onDone()
        }
    }
}



