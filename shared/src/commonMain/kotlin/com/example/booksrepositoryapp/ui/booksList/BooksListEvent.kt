package com.example.booksrepositoryapp.ui.booksList

sealed class BooksListEvent {
    data class SearchQueryChanged(val query: String): BooksListEvent()
    data class BookClicked(val bookId: String): BooksListEvent()
    data class FilterByPrice(val minPrice: Int, val maxPrice: Int): BooksListEvent()
    object RefreshBooks: BooksListEvent()
    object BackClicked: BooksListEvent()
}