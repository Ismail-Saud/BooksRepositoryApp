package com.example.booksrepositoryapp.ui.auth.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel (application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    private val authRepo = AuthRepository()
    private val _registerUser = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerUser: StateFlow<RegisterState> = _registerUser.asStateFlow()
    private val usernamePattern = Regex("^[A-Za-z0-9!@#$]+$")
    private val passwordPattern = Regex("^[A-Za-z0-9!@#$]{8,}$")
    private val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun register (username: String, email: String, password:String, confirmPass: String) {
        when {
            username.isBlank() -> {
                _registerUser.value = RegisterState.Error("Enter username")
            }
            !usernamePattern.matches(username) -> {
                _registerUser.value = RegisterState.Error("Enter valid username")
            }
            email.isBlank() -> {
                _registerUser.value = RegisterState.Error("Enter email")
            }
            !emailPattern.matches(email) -> {
                _registerUser.value = RegisterState.Error("Enter valid email")
            }
            password.isBlank() -> {
                _registerUser.value = RegisterState.Error("Enter password")
            }
            !passwordPattern.matches(password) -> {
                _registerUser.value = RegisterState.Error("Enter valid password")
            }
            password != confirmPass -> {
                _registerUser.value = RegisterState.Error("Password does not match")
            }
            else -> {
//                val user = UserModel(username = userName, email = email, password = password)
//                val id = userRepo.signupUser(user)
//                userRepo.setUserSaved(id.toInt())
                viewModelScope.launch {
                    _registerUser.value = RegisterState.Loading
                    val result = authRepo.createUser(username, email, password)
                    result.onSuccess { uid ->
                        val profile = UserProfile(
                            uid = uid,
                            username = username,
                            email = email,
                            profilePicture = null
                        )
                        userRepo.createUserProfile(profile)
                        userRepo.setLoggedIn(true)
                        _registerUser.value = RegisterState.Success
                    }
                    result.onFailure { exception ->
                        _registerUser.value = RegisterState.Error(exception.message ?: "Registration Failed")
                    }
                }
            }
        }
    }
}