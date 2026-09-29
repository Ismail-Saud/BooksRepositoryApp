package com.example.booksrepositoryapp.ui.addToCart

import com.example.booksrepositoryapp.domain.model.Cart

sealed class AddToCartState {
    object Idle: AddToCartState()
    object Loading: AddToCartState()
    data class Success(val cart: List<Cart>): AddToCartState()
    data class Error(val message: String): AddToCartState()
}
