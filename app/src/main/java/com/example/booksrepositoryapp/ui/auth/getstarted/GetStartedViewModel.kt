package com.example.booksrepositoryapp.ui.auth.getstarted

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetStartedViewModel (application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    private val _getStartedState = MutableStateFlow<GetStartedState>(GetStartedState.Idle)
    val getStartedState: Flow<GetStartedState> = _getStartedState.asStateFlow()

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
                    val user = userRepo.loginUser(email, password)
                    if (user != null) {
                        _getStartedState.value = GetStartedState.Success
                        userRepo.setLoggedIn(true)
                        userRepo.setUserSaved(user.id)
                    } else {
                        _getStartedState.value = GetStartedState.Error("Invalid email or password")
                    }
                }
            }
        }
    }
}