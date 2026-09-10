package com.example.booksrepositoryapp.ui.auth.getStarted

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GetStartedViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository()
    private val userRepo = UserRepositoryImpl(application)
    private val _getStartedState = MutableStateFlow<GetStartedState>(GetStartedState.Idle)
    val getStartedState: StateFlow<GetStartedState> = _getStartedState.asStateFlow()
    private val _effect = Channel<GetStartedEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: GetStartedEvent) {
        when (event) {
            is GetStartedEvent.GetStartedClicked -> login(event.email, event.password)
            GetStartedEvent.BackClicked -> sendEffect(GetStartedEffect.NavigateBack)
            GetStartedEvent.RegisterClicked -> sendEffect(GetStartedEffect.NavigateToRegister)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _getStartedState.value = GetStartedState.Loading
            val result = authRepo.login(email, password)
            result.onSuccess {
                userRepo.setLoggedIn(true)
                _getStartedState.value = GetStartedState.Success
                sendEffect(GetStartedEffect.ShowToast("Login Successful"))
                sendEffect(GetStartedEffect.NavigateToHome)
            }
            result.onFailure { exception ->
                _getStartedState.value = GetStartedState.Error(exception.message ?: "Login Failed")
                sendEffect(GetStartedEffect.ShowToast(exception.message ?: "Login Failed"))
            }
        }
    }

    private fun sendEffect(effect: GetStartedEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
