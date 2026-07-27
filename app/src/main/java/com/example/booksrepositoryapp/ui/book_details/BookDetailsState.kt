package com.example.booksrepositoryapp.ui.book_details

import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel

sealed class BookDetailsState {
    object Idle: BookDetailsState()
    object Loading: BookDetailsState()
    data class Error(val message: String): BookDetailsState()
    data class Success(val books: BookDetailsModel?): BookDetailsState()
}