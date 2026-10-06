package com.example.booksrepositoryapp.ui.bookDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel (
    private val authRepo: AuthRepository,
    private val bookRepo: BooksRepository,
    private val cartRepo: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _bookDetailState = MutableStateFlow<BookDetailsState>(BookDetailsState.Idle)
    val bookDetailState = _bookDetailState.asStateFlow()

    private val _effect = Channel<BookDetailsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val workId: String = savedStateHandle["workId"] ?: ""

    init {
        if (workId.isNotEmpty()) {
            getBookDetails(workId)
        }
    }

    fun onEvent(event: BookDetailsEvent) {
        when (event) {
            BookDetailsEvent.AddToCartClicked -> addToCart()
            BookDetailsEvent.BackClicked -> {
                _effect.trySend(BookDetailsEffect.NavigateBack)
            }
            is BookDetailsEvent.LoadBookDetails -> getBookDetails(event.workId)
        }
    }

    fun getBookDetails(key: String) {
        viewModelScope.launch {
            _bookDetailState.value = BookDetailsState.Loading
            bookRepo.updateBook(key)
            val response = bookRepo.getBookDetails(key)
            _bookDetailState.value = BookDetailsState.Success(response)
        }
    }

    private fun addToCart() {
        val userId = authRepo.getCurrentUserId() ?: ""
        val state = _bookDetailState.value
        if (state is BookDetailsState.Success && state.books != null) {
            if (state.isAddingToCart) return
            val book = state.books
            _bookDetailState.value = state.copy(isAddingToCart = true)
            val cart = Cart(
                bookId = book.id,
                title = book.title,
                author = book.author,
                price = book.price ?: 0.0,
                coverId = book.coverId,
                category = book.category,
                quantity = 1
            )
            viewModelScope.launch {
                try {
                    cartRepo.insertCartItem(userId, cart)
                    _effect.send(BookDetailsEffect.ShowMessage("Added to Cart"))
                } catch (e: Exception) {
                    _effect.send(BookDetailsEffect.ShowMessage(e.message ?: "Failed to add to cart"))
                } finally {
                    val currentState = _bookDetailState.value
                    if (currentState is BookDetailsState.Success) {
                        _bookDetailState.value = currentState.copy(isAddingToCart = false)
                    }
                }
            }
        }
    }
}
