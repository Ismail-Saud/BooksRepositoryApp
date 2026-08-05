package com.example.booksrepositoryapp.ui.checkout_screen

import com.example.booksrepositoryapp.data.local.room.entity.AddressModel

sealed class CheckoutState {
    object Idle: CheckoutState()
    object Loading : CheckoutState()
    data class Success(val address: AddressModel?) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}