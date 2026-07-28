package com.example.booksrepositoryapp.ui.checkout_screen

sealed class CheckoutState {
    object Idle: CheckoutState()
    object Success: CheckoutState()
    data class Error(val message: String): CheckoutState()
}