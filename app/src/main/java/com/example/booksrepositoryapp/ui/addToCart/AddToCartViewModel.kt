package com.example.booksrepositoryapp.ui.addToCart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddToCartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepo = CartRepositoryImpl()
    private val authRepo = AuthRepository()
    private val userId = authRepo.getCurrentUserId() ?: ""
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState = _addToCartState.asStateFlow()

    fun getCartItems() {
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            cartRepo.getCart(userId).catch { exception ->
                _addToCartState.value = AddToCartState.Error(exception.message ?: "Something went wrong")
            }.collect { cartItems ->
                _addToCartState.value = AddToCartState.Success(cartItems)
            }
        }
    }

    fun increaseQuantity(cart: Cart) {
        viewModelScope.launch {
            cartRepo.updateCartItem(
                userId = userId,
                bookId = cart.bookId,
                quantity = cart.quantity + 1
            )
        }
    }

    fun decreaseQuantity(cart: Cart) {
        if (cart.quantity > 1) {
            viewModelScope.launch {
                cartRepo.updateCartItem(
                    userId = userId,
                    bookId = cart.bookId,
                    quantity = cart.quantity - 1
                )
            }
        }
    }

    fun removeCartItem(cart: Cart) {
        viewModelScope.launch {
            cartRepo.deleteCartItem(
                userId = userId,
                bookId = cart.bookId,
            )
        }
    }
}
