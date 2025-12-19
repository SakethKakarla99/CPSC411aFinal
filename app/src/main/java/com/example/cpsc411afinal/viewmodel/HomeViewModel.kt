package com.example.cpsc411afinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsc411afinal.data.repo.AuthRepository
import com.example.cpsc411afinal.data.repo.UserPrefsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val displayName: String
)

class HomeViewModel(
    authRepo: AuthRepository,
    prefsRepo: UserPrefsRepository
) : ViewModel() {

    val ui: StateFlow<HomeUiState> =
        combine(
            prefsRepo.displayName,
            kotlinx.coroutines.flow.flowOf(authRepo.currentEmail())
        ) { name, _ ->
            HomeUiState(
                displayName = if (name.isBlank()) "User" else name
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeUiState("User")
        )
}


