package com.example.booksrepositoryapp.data.source.local.localDataSources

import com.example.booksrepositoryapp.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksLocalDataSource {
    fun getBooksByCategory(category: String): Flow<List<Book>>
    suspend fun getAllBooksByCategory(category: String): List<Book>
    suspend fun insertBooks(books: List<Book>)
    suspend fun getBookDetails(id: String): Book?
    suspend fun updateBookDescription(id: String, description: String?)
}