package com.example.cpsc411afinal.data.repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    fun isLoggedIn(): Boolean = auth.currentUser != null
    fun currentEmail(): String? = auth.currentUser?.email
    fun signOut() = auth.signOut()

    suspend fun signUp(email: String, password: String): AuthResult =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign up failed.")
        }

    suspend fun login(email: String, password: String): AuthResult =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed.")
        }
}
