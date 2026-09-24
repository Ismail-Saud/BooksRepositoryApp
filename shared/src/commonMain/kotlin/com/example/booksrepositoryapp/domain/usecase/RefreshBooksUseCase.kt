package com.example.booksrepositoryapp.domain.usecase

import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.repository.BooksRepository

class RefreshBooksUseCase(private val repository: BooksRepository) {
    suspend operator fun invoke(subject: String): RefreshResult {
        return repository.refreshBooks(subject)
    }
}
