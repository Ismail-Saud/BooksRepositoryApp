package com.example.booksrepositoryapp.ui.bookDetails

import com.example.booksrepositoryapp.domain.model.Book

sealed class BookDetailsState {
    object Idle: BookDetailsState()
    object Loading: BookDetailsState()
    data class Error(val message: String): BookDetailsState()
    data class Success(val books: Book?): BookDetailsState()
}
