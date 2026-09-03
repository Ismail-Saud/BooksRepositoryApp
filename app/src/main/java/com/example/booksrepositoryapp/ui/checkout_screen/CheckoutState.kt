package com.example.booksrepositoryapp.ui.checkout_screen

import com.example.booksrepositoryapp.data.firebase.firestore.AddressModelFB

sealed class CheckoutState {
    object Idle: CheckoutState()
    object Loading : CheckoutState()
    data class Success(val address: AddressModelFB?) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
