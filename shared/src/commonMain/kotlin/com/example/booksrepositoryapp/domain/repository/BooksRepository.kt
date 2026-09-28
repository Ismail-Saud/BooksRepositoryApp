package com.example.booksrepositoryapp.domain.repository

import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getBooks(category: String): Flow<List<Book>>
    suspend fun refreshBooks(subject: String, limit: Int? = null, offset: Int? = null): RefreshResult
    suspend fun getBookDetails(id: String): Book?
    suspend fun updateBook(id: String): RefreshResult
}
