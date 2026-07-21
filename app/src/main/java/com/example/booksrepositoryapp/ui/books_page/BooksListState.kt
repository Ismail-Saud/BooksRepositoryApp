package com.example.booksrepositoryapp.ui.books_page

import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.SubjectApiResponseModel
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work

sealed class BooksListState {
    object Idle: BooksListState()
    object Loading: BooksListState()
    data class Error(val message: String): BooksListState()
    data class Success(val books: List<Work>): BooksListState()
}