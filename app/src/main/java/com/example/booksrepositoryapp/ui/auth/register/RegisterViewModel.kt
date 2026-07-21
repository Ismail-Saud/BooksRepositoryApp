package com.example.booksrepositoryapp.ui.auth.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel (application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository(application)
    private val _registerUser = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerUser: Flow<RegisterState> = _registerUser.asStateFlow()

    private val usernamePattern = Regex("^[A-Za-z0-9!@#$]+$")
    private val passwordPattern = Regex("^[A-Za-z0-9!@#$]{8,}$")

    suspend fun register (userName: String, email: String, password:String) {
        when {
            userName.isBlank() -> {
                _registerUser.value = RegisterState.Error("Enter username")
            }
            !usernamePattern.matches(userName) -> {
                _registerUser.value = RegisterState.Error("Enter valid username")
            }
            email.isBlank() -> {
                _registerUser.value = RegisterState.Error("Enter email")
            }
            password.isBlank() -> {
                _registerUser.value = RegisterState.Error("Enter username")
            }
            !passwordPattern.matches(password) -> {
                _registerUser.value = RegisterState.Error("Enter valid password")
            }
            userRepo.doesEmailExist(email) -> {
                _registerUser.value = RegisterState.Error("User already exists")
            }
            else -> {
                val user = UserModel(username = userName, email = email, password = password)
                userRepo.signupUser(user)
                userRepo.setLoggedIn(true)
                _registerUser.value = RegisterState.Success
            }
        }
    }
}