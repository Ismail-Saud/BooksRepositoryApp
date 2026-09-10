package com.example.booksrepositoryapp.ui.booksList

sealed class BooksListEffect {
    data class NavigateToBookDetails(val workId: String): BooksListEffect()
    data class ShowError(val message: String): BooksListEffect()
    object NavigateBack: BooksListEffect()
}