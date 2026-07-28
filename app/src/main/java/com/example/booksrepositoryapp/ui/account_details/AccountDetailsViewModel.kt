package com.example.booksrepositoryapp.ui.account_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository(application)
    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    fun getUser() {
        val id = userRepo.getSavedUser()?.toInt() ?: 1
        viewModelScope.launch {
            userRepo.getUserDetails(id).collect { user ->
                _user.value = user
            }
        }
    }

    fun logout() {
        userRepo.setLoggedIn(false)
    }
}