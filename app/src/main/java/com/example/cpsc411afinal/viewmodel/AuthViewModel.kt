package com.example.cpsc411afinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsc411afinal.data.repo.AuthRepository
import com.example.cpsc411afinal.data.repo.AuthResult
import com.example.cpsc411afinal.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    private val _loggedIn = MutableStateFlow(repo.isLoggedIn())
    val loggedIn: StateFlow<Boolean> = _loggedIn

    fun setEmail(v: String) = _ui.update { it.copy(email = v, emailError = null, errorMessage = null) }
    fun setPassword(v: String) = _ui.update { it.copy(password = v, passwordError = null, errorMessage = null) }

    private fun validate(): Boolean {
        val email = ui.value.email.trim()
        val pw = ui.value.password
        var ok = true

        val emailErr = if (!Validators.isValidEmail(email)) { ok = false; "Invalid email format." } else null
        val pwErr = if (!Validators.isValidPassword(pw)) { ok = false; "Password must be 6+ characters." } else null

        _ui.update { it.copy(emailError = emailErr, passwordError = pwErr) }
        return ok
    }

    fun login() {
        if (!validate()) return
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, errorMessage = null) }
            val res = repo.login(ui.value.email.trim(), ui.value.password)
            _ui.update { it.copy(loading = false) }
            when (res) {
                is AuthResult.Success -> _loggedIn.value = true
                is AuthResult.Error -> _ui.update { it.copy(errorMessage = res.message) }
            }
        }
    }

    fun signUp() {
        if (!validate()) return
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, errorMessage = null) }
            val res = repo.signUp(ui.value.email.trim(), ui.value.password)
            _ui.update { it.copy(loading = false) }
            when (res) {
                is AuthResult.Success -> _loggedIn.value = true
                is AuthResult.Error -> _ui.update { it.copy(errorMessage = res.message) }
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _loggedIn.value = false
        _ui.value = AuthUiState()
    }
}