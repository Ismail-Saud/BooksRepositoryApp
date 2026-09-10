package com.example.booksrepositoryapp.ui.bookDetails

sealed class BookDetailsEffect {
    data class ShowToast(val message: String): BookDetailsEffect()
    object NavigateBack: BookDetailsEffect()
}