package com.example.booksrepositoryapp.ui.addToCart

sealed class AddToCartEffect {
    data class NavigateToCheckout(val total: Double): AddToCartEffect()
    data class ShowToast(val message: String): AddToCartEffect()
}