package com.example.booksrepositoryapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.booksrepositoryapp.data.repository.UserRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    val isLoggedIn = userRepo.loginState
}