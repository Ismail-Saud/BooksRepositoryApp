package com.example.booksrepositoryapp.ui.auth.register

sealed class RegisterEffect {
    object NavigateToHome: RegisterEffect()
    object NavigateBack: RegisterEffect()
    object NavigateToGetStarted: RegisterEffect()
    data class ShowMessage(val message: String): RegisterEffect()
}