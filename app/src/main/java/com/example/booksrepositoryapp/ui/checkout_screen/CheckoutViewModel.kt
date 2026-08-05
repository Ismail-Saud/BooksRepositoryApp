package com.example.booksrepositoryapp.ui.checkout_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.data.repository.AddressRepository
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository.getInstance(application)
    private val addressRepo = AddressRepository(application)
    private val cartRepo = CartRepository(application)

    val userId = userRepo.getSavedUser()?.toInt() ?: 1

    val address: Flow<AddressModel?> = addressRepo.getSelectedAddress(userId)

    fun deleteAddress() {
        viewModelScope.launch {
            addressRepo.deleteAllAddresses()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepo.clearCart()
        }
    }
}