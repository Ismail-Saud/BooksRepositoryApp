package com.example.booksrepositoryapp.ui.auth.getstarted

sealed class GetStartedState {
    object Idle: GetStartedState()
    object Loading: GetStartedState()
    data class Error (val message: String): GetStartedState()
    object Success: GetStartedState()
}