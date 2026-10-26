package com.example.booksrepositoryapp.domain.usecase

import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first

class BooksUseCasesTest {
    @Test
    fun getBooksDelegatesTheCategoryToRepository() = runTest {
        val repository = RecordingBooksRepository()
        val expected: List<Book> = listOf(book(id = "one"))
        repository.books = expected

        val result = GetBooksUseCase(repository)("fantasy").first()

        assertEquals(expected, result)
        assertEquals("fantasy", repository.requestedCategory)
    }

    @Test
    fun refreshBooksDelegatesTheSubjectAndReturnsRepositoryResult() = runTest {
        val repository = RecordingBooksRepository()
        repository.refreshResult = RefreshResult.Offline

        val result = RefreshBooksUseCase(repository)("history")

        assertEquals(RefreshResult.Offline, result)
        assertEquals("history", repository.refreshedSubject)
    }
}

private class RecordingBooksRepository : BooksRepository {
    var books: List<Book> = emptyList()
    var requestedCategory: String? = null
    var refreshedSubject: String? = null
    var refreshResult: RefreshResult = RefreshResult.Success

    override fun getBooks(category: String): Flow<List<Book>> {
        requestedCategory = category
        return flowOf(books)
    }

    override suspend fun refreshBooks(subject: String, limit: Int?, offset: Int?): RefreshResult {
        refreshedSubject = subject
        return refreshResult
    }

    override suspend fun getBookDetails(id: String): Book? = null

    override suspend fun updateBook(id: String): RefreshResult = RefreshResult.Success
}

private fun book(id: String) = Book(
    id = id,
    category = "fantasy",
    title = "Title",
    author = "Author",
    coverId = 0,
    rating = null,
    price = null,
    description = null,
)
