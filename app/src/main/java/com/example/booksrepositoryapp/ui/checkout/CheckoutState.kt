package com.example.booksrepositoryapp.ui.checkout

import com.example.booksrepositoryapp.domain.model.Address

sealed class CheckoutState {
    object Idle: CheckoutState()
    object Loading : CheckoutState()
    data class Success(val address: Address?) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
