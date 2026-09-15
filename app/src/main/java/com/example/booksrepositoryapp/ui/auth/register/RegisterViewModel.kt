package com.example.booksrepositoryapp.ui.auth.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    // Injected manually for now, should be Hilt later
    private val userRepo = UserRepositoryImpl(application)
    private val authRepo = AuthRepository()
    
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
        when {
            username.isBlank() -> sendError("Enter username")
            !usernamePattern.matches(username) -> sendError("Enter valid username")
            email.isBlank() -> sendError("Enter email")
            !emailPattern.matches(email) -> sendError("Enter valid email")
            password.isBlank() -> sendError("Enter password")
            !passwordPattern.matches(password) -> sendError("Enter valid password")
            password != confirmPass -> sendError("Password does not match")
            else -> {
                viewModelScope.launch {
                    _registerUser.value = RegisterState.Loading
                    try {
                        val result = authRepo.createUser(username, email, password)
                        result.onSuccess { uid ->
                            val domainUser = User(
                                id = uid,
                                username = username,
                                email = email,
                                profilePicture = null
                            )
                            userRepo.createUserProfile(domainUser)
                            _registerUser.value = RegisterState.Success
                            sendEffect(RegisterEffect.ShowToast("Signup Successful"))
                            sendEffect(RegisterEffect.NavigateToHome)
                        }
                        result.onFailure { exception ->
                            val errorMessage = exception.message ?: "Registration Failed"
                            _registerUser.value = RegisterState.Error(errorMessage)
                            sendEffect(RegisterEffect.ShowToast(errorMessage))
                        }
                    } catch (e: Exception) {
                        val errorMessage = e.message ?: "An unexpected error occurred"
                        _registerUser.value = RegisterState.Error(errorMessage)
                        sendEffect(RegisterEffect.ShowToast(errorMessage))
                    }
                }
            }
        }
    }

    private fun sendError(message: String) {
        _registerUser.value = RegisterState.Error(message)
        _effect.trySend(RegisterEffect.ShowToast(message))
    }

    private fun sendEffect(effect: RegisterEffect) {
        _effect.trySend(effect)
    }
}
