package com.example.booksrepositoryapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.User
import com.example.booksrepositoryapp.domain.repository.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel (
    private val userRepo: UserRepository,
    private val authRepo: AuthRepository,
): ViewModel() {
    private val _registerUser = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerUser: StateFlow<RegisterState> = _registerUser.asStateFlow()
    private val _effect = Channel<RegisterEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    
    private val usernamePattern = Regex("^[A-Za-z0-9!@#$]+$")
    private val passwordPattern = Regex("^[A-Za-z0-9!@#$]{8,}$")
    private val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.RegisterClicked -> register(event.username, event.email, event.password, event.confirmPass)
            RegisterEvent.BackClicked -> _effect.trySend(RegisterEffect.NavigateBack)
            RegisterEvent.GetStartedClicked -> _effect.trySend(RegisterEffect.NavigateToGetStarted)
        }
    }

    fun register(username: String, email: String, password: String, confirmPass: String) {
        val trimmedUsername = username.trim()
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedConfirmPass = confirmPass.trim()
        when {
            trimmedUsername.isBlank() -> sendError("Enter username")
            !usernamePattern.matches(trimmedUsername) -> sendError("Enter valid username")
            trimmedEmail.isBlank() -> sendError("Enter email")
            !emailPattern.matches(trimmedEmail) -> sendError("Enter valid email")
            trimmedPassword.isBlank() -> sendError("Enter password")
            !passwordPattern.matches(trimmedPassword) -> sendError("Password must be at least 8 characters")
            trimmedPassword != trimmedConfirmPass -> sendError("Passwords do not match")
            else -> {
                viewModelScope.launch {
                    _registerUser.value = RegisterState.Loading
                    try {
                        val result = authRepo.createUser(trimmedUsername, trimmedEmail, trimmedPassword)
                        result.onSuccess { uid ->
                            val domainUser = User(
                                id = uid,
                                username = trimmedUsername,
                                email = trimmedEmail,
                                profilePicture = null
                            )
                            userRepo.createUserProfile(domainUser)
                            _registerUser.value = RegisterState.Success
                            sendEffect(RegisterEffect.ShowMessage("Signup Successful"))
                            sendEffect(RegisterEffect.NavigateToHome)
                        }
                        result.onFailure { exception ->
                            val errorMessage = exception.message ?: "Registration Failed"
                            _registerUser.value = RegisterState.Error(errorMessage)
                            sendEffect(RegisterEffect.ShowMessage(errorMessage))
                        }
                    } catch (e: Exception) {
                        val errorMessage = e.message ?: "An unexpected error occurred"
                        _registerUser.value = RegisterState.Error(errorMessage)
                        sendEffect(RegisterEffect.ShowMessage(errorMessage))
                    }
                }
            }
        }
    }

    private fun sendError(message: String) {
        _registerUser.value = RegisterState.Error(message)
        _effect.trySend(RegisterEffect.ShowMessage(message))
    }

    private fun sendEffect(effect: RegisterEffect) {
        _effect.trySend(effect)
    }
}
