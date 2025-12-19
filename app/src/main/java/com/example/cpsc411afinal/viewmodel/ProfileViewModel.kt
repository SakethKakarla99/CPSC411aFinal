package com.example.cpsc411afinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsc411afinal.data.repo.UserPrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val email: String = "",
    val name: String = "",
    val nameError: String? = null,
    val saving: Boolean = false,
    val savedMessage: String? = null
)

class ProfileViewModel(
    private val repo: UserPrefsRepository,
    private val emailProvider: () -> String
) : ViewModel() {

    private val _ui = MutableStateFlow(ProfileUiState(email = emailProvider()))
    val ui: StateFlow<ProfileUiState> = _ui

    init {
        viewModelScope.launch {
            val current = repo.displayName.first()
            _ui.update { it.copy(name = current) }
        }
    }

    fun setName(v: String) {
        _ui.update { it.copy(name = v, nameError = null, savedMessage = null) }
    }

    private fun validate(): Boolean {
        val n = ui.value.name.trim()
        if (n.isEmpty()) {
            _ui.update { it.copy(nameError = "Name cannot be empty.") }
            return false
        }
        return true
    }

    fun save() {
        if (!validate()) return
        viewModelScope.launch {
            _ui.update { it.copy(saving = true, savedMessage = null) }
            repo.setDisplayName(ui.value.name.trim())
            _ui.update { it.copy(saving = false, savedMessage = "Saved ✅") }
        }
    }
}


