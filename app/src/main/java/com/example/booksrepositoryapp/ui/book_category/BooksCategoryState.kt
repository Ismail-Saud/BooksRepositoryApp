package com.example.booksrepositoryapp.ui.book_category

import com.example.booksrepositoryapp.data.api.models.Category

sealed class BooksCategoryState {
    object Idle: BooksCategoryState()
    object Loading: BooksCategoryState()
    data class Success(val categories: List<Category>): BooksCategoryState()
    data class Error(val message: String): BooksCategoryState()
}