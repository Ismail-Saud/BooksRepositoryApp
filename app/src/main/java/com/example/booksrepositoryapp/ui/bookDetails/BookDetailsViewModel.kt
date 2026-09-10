package com.example.booksrepositoryapp.ui.bookDetails

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Cart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _bookDetailState = MutableStateFlow<BookDetailsState>(BookDetailsState.Idle)
    val bookDetailState = _bookDetailState.asStateFlow()

    private val _effect = Channel<BookDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    private val authRepo = AuthRepository()
    private val bookRepo = BooksRepositoryImpl(application)
    private val cartRepo = CartRepositoryImpl()

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
        val userId = authRepo.getCurrentUserId() ?: ""
        val state = _bookDetailState.value
        if (state is BookDetailsState.Success && state.books != null) {
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
            viewModelScope.launch {
                cartRepo.insertCartItem(userId, cart)
                _effect.send(BookDetailsEffect.ShowToast("Added to Cart"))
            }
        }
    }

    fun resetState() {
        _bookDetailState.value = BookDetailsState.Idle
    }
}
