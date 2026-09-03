package com.example.booksrepositoryapp.ui.cart_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.firebase.firestore.CartModelFB
import com.example.booksrepositoryapp.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddToCartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepo = CartRepository(application)
    private val authRepo = AuthRepository()
    private val userId = authRepo.getCurrentUserId() ?: ""
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState = _addToCartState.asStateFlow()

    fun getCartItems () {
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            cartRepo.getCart(userId).catch { exception ->
                _addToCartState.value = AddToCartState.Error(exception.message?:"Something went wrong")
            }.collect { cartItems ->
                _addToCartState.value = AddToCartState.Success(cartItems)
            }
        }
    }

    fun increaseQuantity (cartItem: CartModelFB) {
        viewModelScope.launch {
            cartRepo.updateCartItem(
                userId = userId,
                bookId = cartItem.workId,
                quantity = cartItem.quantity + 1
            )
        }
    }

    fun decreaseQuantity (cartItem: CartModelFB) {
        if (cartItem.quantity > 1) {
            viewModelScope.launch {
                cartRepo.updateCartItem(
                    userId = userId,
                    bookId = cartItem.workId,
                    quantity = cartItem.quantity - 1
                )
            }
        }
    }

    fun removeCartItem (cartItem: CartModelFB) {
        viewModelScope.launch {
            cartRepo.deleteCartItem(
                userId = userId,
                bookId = cartItem.workId,
            )
        }
    }
}