package com.example.booksrepositoryapp.ui.checkout_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    val userRepo = UserRepository(application)

    fun saveAddress(address: String) {
        val userId = userRepo.getSavedUser()?.toInt() ?: 1
        viewModelScope.launch {
            userRepo.updateAddress(userId, address)
        }
    }

    suspend fun getAddress() : String? {
        val userId = userRepo.getSavedUser()?.toInt() ?: 1
        return userRepo.getAddress(userId)
    }
}