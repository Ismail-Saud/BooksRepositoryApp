package com.example.booksrepositoryapp.ui.addToCart

import com.example.booksrepositoryapp.domain.model.Cart

sealed class AddToCartEvent {
    data class IncreaseQuantity(val cart: Cart): AddToCartEvent()
    data class DecreaseQuantity(val cart: Cart): AddToCartEvent()
    data class RemoveItem(val cart: Cart): AddToCartEvent()
    data class ProceedToCheckout(val total: Double): AddToCartEvent()
}