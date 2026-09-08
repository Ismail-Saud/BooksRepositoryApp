package com.example.booksrepositoryapp.domain.usecase

import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase(private val repository: BooksRepository) {
    operator fun invoke(category: String): Flow<List<Book>> {
        return repository.getBooks(category)
    }
}
