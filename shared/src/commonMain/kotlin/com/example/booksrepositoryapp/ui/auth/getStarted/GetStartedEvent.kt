package com.example.booksrepositoryapp.ui.auth.getStarted

sealed class GetStartedEvent {
    data class GetStartedClicked(val email: String, val password: String): GetStartedEvent()
    object BackClicked: GetStartedEvent()
    object RegisterClicked: GetStartedEvent()
}