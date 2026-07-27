package com.example.booksrepositoryapp.ui.book_details

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepository
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val _bookDetailState = MutableStateFlow<BookDetailsState>(BookDetailsState.Idle)
    val bookDetailState = _bookDetailState.asStateFlow()
    private val userRepo = UserRepository(application)
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
        val userId = userRepo.getSavedUser()?.toInt() ?: 1
        viewModelScope.launch {
            cartRepo.insertCartItem(userId, bookId)
        }
    }

    fun resetState () {
        _bookDetailState.value = BookDetailsState.Idle
    }
}