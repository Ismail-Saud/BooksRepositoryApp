package com.example.booksrepositoryapp.ui.auth.getStarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GetStartedViewModel (
    private val authRepo: AuthRepository,
): ViewModel() {
    private val _getStartedState = MutableStateFlow<GetStartedState>(GetStartedState.Idle)
    val getStartedState: StateFlow<GetStartedState> = _getStartedState.asStateFlow()
    private val _effect = Channel<GetStartedEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun onEvent(event: GetStartedEvent) {
        when (event) {
            is GetStartedEvent.GetStartedClicked -> login(event.email, event.password)
            GetStartedEvent.BackClicked -> sendEffect(GetStartedEffect.NavigateBack)
            GetStartedEvent.RegisterClicked -> sendEffect(GetStartedEffect.NavigateToRegister)
        }
    }

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        when {
            trimmedEmail.isBlank() -> sendError("Enter email")
            !emailPattern.matches(trimmedEmail) -> sendError("Enter valid email")
            trimmedPassword.isBlank() -> sendError("Enter password")
            trimmedPassword.length < 6 -> sendError("Password must be at least 6 characters")
            else -> {
                viewModelScope.launch {
                    _getStartedState.value = GetStartedState.Loading
                    val result = authRepo.login(trimmedEmail, trimmedPassword)
                    result.onSuccess {
                        _getStartedState.value = GetStartedState.Success
                        sendEffect(GetStartedEffect.ShowMessage("Login Successful"))
                        sendEffect(GetStartedEffect.NavigateToHome)
                    }
                    result.onFailure { exception ->
                        val errorMessage = exception.message ?: "Login Failed"
                        _getStartedState.value = GetStartedState.Error(errorMessage)
                        sendEffect(GetStartedEffect.ShowMessage(errorMessage))
                    }
                }
            }
        }
    }

    private fun sendError(message: String) {
        _getStartedState.value = GetStartedState.Error(message)
        sendEffect(GetStartedEffect.ShowMessage(message))
    }

    private fun sendEffect(effect: GetStartedEffect) {
        _effect.trySend(effect)
    }
}
