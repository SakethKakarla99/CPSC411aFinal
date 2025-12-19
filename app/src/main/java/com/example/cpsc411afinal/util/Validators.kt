package com.example.cpsc411afinal.util

object Validators {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun isValidEmail(email: String) = emailRegex.matches(email.trim())
    fun isValidPassword(pw: String) = pw.length >= 6
}