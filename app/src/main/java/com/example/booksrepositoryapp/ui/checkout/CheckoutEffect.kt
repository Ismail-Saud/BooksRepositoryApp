package com.example.booksrepositoryapp.ui.checkout

sealed class CheckoutEffect {
    object NavigateBack: CheckoutEffect()
    object NavigateToAddressList: CheckoutEffect()
    object NavigateToSuccess: CheckoutEffect()
    data class ShowError(val message: String): CheckoutEffect()
}