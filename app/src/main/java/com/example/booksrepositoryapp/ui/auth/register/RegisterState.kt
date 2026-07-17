package com.example.booksrepositoryapp.ui.auth.register

sealed class RegisterState {
    object Idle: RegisterState()
    data class Error (val message: String) : RegisterState()
    object Success: RegisterState()
}