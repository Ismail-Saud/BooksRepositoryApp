package com.example.booksrepositoryapp.ui.auth.getstarted

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetStartedViewModel (application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    private val authRepo = AuthRepository()
    private val _getStartedState = MutableStateFlow<GetStartedState>(GetStartedState.Idle)
    val getStartedState: StateFlow<GetStartedState> = _getStartedState.asStateFlow()

    fun login (email: String, password: String) {
        when {
            email.isBlank() -> {
                _getStartedState.value = GetStartedState.Error("Enter email")
            }
            password.isBlank() -> {
                _getStartedState.value = GetStartedState.Error("Enter password")
            }
            else -> {
                viewModelScope.launch {
                    _getStartedState.value = GetStartedState.Loading
                    val result = authRepo.login(email, password)
                    result.onSuccess {
                        _getStartedState.value = GetStartedState.Success
                    }
                    result.onFailure { exception ->
                        _getStartedState.value = GetStartedState.Error(exception.message ?: "Login Failed")
                    }
                }
            }
        }
    }
}