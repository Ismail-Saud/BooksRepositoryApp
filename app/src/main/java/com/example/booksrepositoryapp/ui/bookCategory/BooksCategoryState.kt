package com.example.booksrepositoryapp.ui.bookCategory

import com.example.booksrepositoryapp.data.source.remote.retrofit.dto.Category

sealed class BooksCategoryState {
    object Idle: BooksCategoryState()
    object Loading: BooksCategoryState()
    data class Success(val categories: List<Category>): BooksCategoryState()
    data class Error(val message: String): BooksCategoryState()
}