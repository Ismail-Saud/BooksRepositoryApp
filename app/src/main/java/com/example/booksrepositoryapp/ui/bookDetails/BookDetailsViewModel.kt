package com.example.booksrepositoryapp.ui.bookDetails

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val bookRepo: BooksRepository,
    private val cartRepo: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _bookDetailState = MutableStateFlow<BookDetailsState>(BookDetailsState.Idle)
    val bookDetailState = _bookDetailState.asStateFlow()

    private val _effect = Channel<BookDetailsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    private val _isAddingToCart = MutableStateFlow(false)
    val isAddingToCart = _isAddingToCart.asStateFlow()

    private val workId: String = savedStateHandle["workId"] ?: ""

    init {
        if (workId.isNotEmpty()) {
            getBookDetails(workId)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun onEvent(event: BookDetailsEvent) {
        when (event) {
            BookDetailsEvent.AddToCartClicked -> addToCart()
            BookDetailsEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(BookDetailsEffect.NavigateBack)
                }
            }
            is BookDetailsEvent.LoadBookDetails -> getBookDetails(event.workId)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBookDetails(key: String) {
        viewModelScope.launch {
            _bookDetailState.value = BookDetailsState.Loading
            bookRepo.updateBook(key)
            val response = bookRepo.getBookDetails(key)
            _bookDetailState.value = BookDetailsState.Success(response)
        }
    }

    private fun addToCart() {
        if (_isAddingToCart.value) return

        val userId = authRepo.getCurrentUserId() ?: ""
        val state = _bookDetailState.value
        if (userId.isEmpty()) {
            _effect.trySend(
                BookDetailsEffect.ShowToast("Please sign in before adding items to your cart")
            )
            return
        }
        if (state !is BookDetailsState.Success || state.books == null) return

        val book = state.books
        val cart = Cart(
            bookId = book.id,
            title = book.title,
            author = book.author,
            price = book.price ?: 0.0,
            coverId = book.coverId,
            category = book.category,
            quantity = 1
        )
        _isAddingToCart.value = true
        viewModelScope.launch {
            try {
                cartRepo.insertCartItem(userId, cart)
                _effect.trySend(BookDetailsEffect.ShowToast("Added to Cart"))
            } catch (exception: Exception) {
                _effect.trySend(
                    BookDetailsEffect.ShowToast(
                        exception.message ?: "Unable to add this book to your cart"
                    )
                )
            } finally {
                _isAddingToCart.value = false
            }
        }
    }

    fun resetState() {
        _bookDetailState.value = BookDetailsState.Idle
    }
}
