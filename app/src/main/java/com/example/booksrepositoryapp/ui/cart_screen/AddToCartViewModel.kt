package com.example.booksrepositoryapp.ui.cart_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.CartModel
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.ui.book_details.BookDetailsState
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddToCartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepo = CartRepository(application)
    private val userRepo = UserRepository.getInstance(application)
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState = _addToCartState.asStateFlow()

    fun getCartItems () {
        val userId = userRepo.getSavedUser()?.toInt() ?: 1
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            cartRepo.getCart(userId).catch { exception ->
                _addToCartState.value = AddToCartState.Error(exception.message?:"Something went wrong")
            }.collect { cartItems ->
                _addToCartState.value = AddToCartState.Success(cartItems)
            }
        }
    }

    fun increaseQuantity (cartItem: CartItem) {
        viewModelScope.launch {
            cartRepo.updateCartItem(
                CartModel(
                    cartId = cartItem.cartId,
                    workId = cartItem.bookId,
                    id = cartItem.userId,
                    quantity = cartItem.quantity + 1
                )
            )
        }
    }

    fun decreaseQuantity (cartItem: CartItem) {
        viewModelScope.launch {
            cartRepo.updateCartItem(
                CartModel(
                    cartId = cartItem.cartId,
                    workId = cartItem.bookId,
                    id = cartItem.userId,
                    quantity = cartItem.quantity - 1
                )
            )
        }
    }

    fun removeCartItem (cartItem: CartItem) {
        viewModelScope.launch {
            cartRepo.deleteCartItem(
                CartModel(
                    cartId = cartItem.cartId,
                    workId = cartItem.bookId,
                    id = cartItem.userId,
                    quantity = cartItem.quantity
                )
            )
        }
    }
}