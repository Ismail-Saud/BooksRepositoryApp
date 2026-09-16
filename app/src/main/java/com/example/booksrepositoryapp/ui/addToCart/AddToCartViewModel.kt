package com.example.booksrepositoryapp.ui.addToCart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToCartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    authRepo: AuthRepository,
) : ViewModel() {
    private val userId = authRepo.getCurrentUserId() ?: ""
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState = _addToCartState.asStateFlow()
    private val _effect = Channel<AddToCartEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    private var cartJob: kotlinx.coroutines.Job? = null
    fun onEvent(event: AddToCartEvent) {
        when (event) {
            is AddToCartEvent.IncreaseQuantity -> increaseQuantity(event.cart)
            is AddToCartEvent.DecreaseQuantity -> decreaseQuantity(event.cart)
            is AddToCartEvent.RemoveItem -> removeCartItem(event.cart)
            is AddToCartEvent.ProceedToCheckout -> {
                _effect.trySend(AddToCartEffect.NavigateToCheckout(event.total))
            }
        }
    }

    fun getCartItems() {
        cartJob?.cancel()
        cartJob = viewModelScope.launch {
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

    init {
        getCartItems()
    }
}
