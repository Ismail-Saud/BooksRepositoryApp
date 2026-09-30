package com.example.booksrepositoryapp.ui.auth.getStarted

sealed class GetStartedEffect {
    object NavigateToHome: GetStartedEffect()
    object NavigateBack: GetStartedEffect()
    object NavigateToRegister: GetStartedEffect()
    data class ShowMessage(val message: String): GetStartedEffect()
}