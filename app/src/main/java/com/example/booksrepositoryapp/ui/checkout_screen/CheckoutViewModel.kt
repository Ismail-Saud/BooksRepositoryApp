package com.example.booksrepositoryapp.ui.checkout_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.data.repository.AddressRepository
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository.getInstance(application)
    private val addressRepo = AddressRepository(application)
    private val cartRepo = CartRepository(application)

    val userId = userRepo.getSavedUser()?.toInt() ?: 1

    val addresses: Flow<List<AddressModel>> =
        addressRepo.getAddresses().map { list ->
            list.filter { it.userId == userId }
        }

    fun addAddress(address: AddressModel) {
        viewModelScope.launch {
            addressRepo.addAddress(address)
        }
    }

    fun updateAddress(address: AddressModel) {
        viewModelScope.launch {
            addressRepo.updateAddress(address)
        }
    }

    fun deleteAddress() {
        viewModelScope.launch {
            addressRepo.deleteAddress()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepo.clearCart()
        }
    }
}