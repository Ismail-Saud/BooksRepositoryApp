package com.example.booksrepositoryapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.repository.UserRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    private val authRepo = AuthRepository()
    val isLoggedIn = userRepo.loginState
}