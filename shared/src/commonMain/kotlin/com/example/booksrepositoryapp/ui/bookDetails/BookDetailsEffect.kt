package com.example.booksrepositoryapp.ui.bookDetails

sealed class BookDetailsEffect {
    data class ShowMessage(val message: String): BookDetailsEffect()
    object NavigateBack: BookDetailsEffect()
}