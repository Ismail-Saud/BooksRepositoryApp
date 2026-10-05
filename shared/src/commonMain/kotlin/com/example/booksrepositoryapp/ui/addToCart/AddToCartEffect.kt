package com.example.booksrepositoryapp.ui.addToCart

sealed class AddToCartEffect {
    data class NavigateToCheckout(val total: Double): AddToCartEffect()
    data class ShowMessage(val message: String): AddToCartEffect()
}