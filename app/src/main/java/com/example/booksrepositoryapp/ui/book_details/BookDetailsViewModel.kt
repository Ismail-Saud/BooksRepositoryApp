package com.example.booksrepositoryapp.ui.book_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val _bookDetailState = MutableStateFlow<BookDetailsState>(BookDetailsState.Idle)
    val bookDetailState = _bookDetailState.asStateFlow()
    private val bookRepo = BooksRepository(application)

    fun getBookDetails(key: String) {
        viewModelScope.launch {
            _bookDetailState.value = BookDetailsState.Loading
            bookRepo.updateBook(key)
            val response = bookRepo.getBookDetails(key)
            _bookDetailState.value = BookDetailsState.Success(response)
        }
    }

    fun resetState () {
        _bookDetailState.value = BookDetailsState.Idle
    }
}