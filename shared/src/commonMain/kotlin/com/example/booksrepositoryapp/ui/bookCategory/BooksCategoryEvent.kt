package com.example.booksrepositoryapp.ui.bookCategory

sealed class BooksCategoryEvent {
    data class SearchQueryChanged(val query: String): BooksCategoryEvent()
    data class CategoryClicked(val apiValue: String, val title: String): BooksCategoryEvent()
    object RefreshCategories: BooksCategoryEvent()
}