package com.example.booksrepositoryapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val userRepo = UserRepositoryImpl(application)
    private val authRepo = AuthRepository()
    val isLoggedIn = userRepo.loginState
}
