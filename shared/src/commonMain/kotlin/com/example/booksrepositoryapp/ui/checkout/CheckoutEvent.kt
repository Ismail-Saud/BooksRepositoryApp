package com.example.booksrepositoryapp.ui.checkout

sealed class CheckoutEvent {
    object BackClick: CheckoutEvent()
    object SelectedAddress: CheckoutEvent()
    data class PayClicked(val selectedPayment: String, val total: Double): CheckoutEvent()
}