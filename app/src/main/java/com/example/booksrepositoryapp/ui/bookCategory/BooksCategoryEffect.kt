package com.example.booksrepositoryapp.ui.bookCategory

sealed class BooksCategoryEffect {
    data class NavigateToBooksList(val apiValue: String, val title: String): BooksCategoryEffect()
    data class ShowError(val message: String): BooksCategoryEffect()
}