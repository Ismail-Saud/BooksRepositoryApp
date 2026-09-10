package com.example.booksrepositoryapp.ui.bookDetails

sealed class BookDetailsEvent {
    object AddToCartClicked: BookDetailsEvent()
    object BackClicked: BookDetailsEvent()
    data class LoadBookDetails(val workId: String): BookDetailsEvent()
}