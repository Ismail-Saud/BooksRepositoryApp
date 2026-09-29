package com.example.booksrepositoryapp.ui.booksList

import com.example.booksrepositoryapp.domain.model.Book

sealed class BooksListState {
    object Idle: BooksListState()
    object Loading: BooksListState()
    object Offline: BooksListState()
    data class Error(val message: String): BooksListState()
    data class Success(val books: List<Book>): BooksListState()
}