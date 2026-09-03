package com.example.booksrepositoryapp.ui.book_details

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.firebase.firestore.CartModelFB
import com.example.booksrepositoryapp.data.repository.BooksRepository
import com.example.booksrepositoryapp.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val _bookDetailState = MutableStateFlow<BookDetailsState>(BookDetailsState.Idle)
    val bookDetailState = _bookDetailState.asStateFlow()
    private val authRepo = AuthRepository()
    private val bookRepo = BooksRepository(application)
    private val cartRepo = CartRepository(application)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBookDetails(key: String) {
        viewModelScope.launch {
            _bookDetailState.value = BookDetailsState.Loading
            bookRepo.updateBook(key)
            val response = bookRepo.getBookDetails(key)
            _bookDetailState.value = BookDetailsState.Success(response)
        }
    }

    fun addToCart (bookId: String) {
        val userId = authRepo.getCurrentUserId() ?: ""
        val state = _bookDetailState.value
        if (state is BookDetailsState.Success && state.books != null) {
            val book = state.books
            val cartItem = CartModelFB(
                workId = book.workId,
                title = book.title,
                author = book.author,
                price = book.price ?: 0.0,
                coverId = book.coverId,
                category = book.category,
                quantity = 1
            )
            viewModelScope.launch {
                cartRepo.insertCartItem(userId, cartItem)
            }
        }
    }

    fun resetState () {
        _bookDetailState.value = BookDetailsState.Idle
    }
}