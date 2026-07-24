package com.example.booksrepositoryapp.ui.books_screen

import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel

sealed class BooksListState {
    object Idle: BooksListState()
    object Loading: BooksListState()
    object Offline: BooksListState()
    data class Error(val message: String): BooksListState()
    data class Success(val books: List<BookDetailsModel>): BooksListState()
}