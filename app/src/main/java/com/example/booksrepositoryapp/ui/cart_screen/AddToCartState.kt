package com.example.booksrepositoryapp.ui.cart_screen

import com.example.booksrepositoryapp.data.local.uiModels.CartItem

sealed class AddToCartState {
    object Idle: AddToCartState()
    object Loading: AddToCartState()
    data class Success(val cartItem: List<CartItem>): AddToCartState()
    data class Error(val message: String): AddToCartState()
}