package com.example.booksrepositoryapp.ui.auth.register

sealed class RegisterEvent {
    data class RegisterClicked(val username: String, val email: String, val password: String, val confirmPass: String): RegisterEvent()
    object BackClicked: RegisterEvent()
    object GetStartedClicked: RegisterEvent()
}