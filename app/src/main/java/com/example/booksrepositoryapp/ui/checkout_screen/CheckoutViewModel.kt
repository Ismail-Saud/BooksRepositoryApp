package com.example.booksrepositoryapp.ui.checkout_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.data.repository.AddressRepository
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository.getInstance(application)
    private val addressRepo = AddressRepository(application)
    private val cartRepo = CartRepository(application)

    val userId = userRepo.getSavedUser()?.toInt() ?: 1
    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Loading)

    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    private val _selectedAddress = MutableStateFlow<AddressModel?>(null)
    val selectedAddress: StateFlow<AddressModel?> = _selectedAddress.asStateFlow()

    init {
        getSelectedAddress()
    }

    private fun getSelectedAddress() {
        viewModelScope.launch {
            addressRepo.getSelectedAddress(userId)
                .catch { error ->
                    _checkoutState.value = CheckoutState.Error(error.message ?: "Something went wrong")
                }
                .collectLatest { address ->
                    _selectedAddress.value = address
                    if (address == null) {
                        _checkoutState.value = CheckoutState.Idle
                    } else {
                        _checkoutState.value = CheckoutState.Success(address)
                    }
                }
        }
    }
    fun clearCart() {
        viewModelScope.launch {
            try {
                cartRepo.clearCart()
            } catch (e: Exception) {
                _checkoutState.value = CheckoutState.Error(e.message ?: "Unable to clear cart")
            }
        }
    }
}