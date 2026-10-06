package com.example.booksrepositoryapp.data.repository

import com.example.booksrepositoryapp.data.source.local.localDataSources.BooksLocalDataSource
import com.example.booksrepositoryapp.data.source.remote.ktor.ApiService
import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import com.example.booksrepositoryapp.manager.networkManager.NetworkManager
import com.example.booksrepositoryapp.util.generateRandomAmount
import com.example.booksrepositoryapp.util.generateRandomRating
import kotlinx.coroutines.flow.Flow

class BooksRepositoryImpl(
    private val booksApi: ApiService,
    private val localDataSource: BooksLocalDataSource,
    private val networkHelper: NetworkManager
) : BooksRepository {

    override fun getBooks(category: String): Flow<List<Book>> {
        return localDataSource.getBooksByCategory(category)
    }

    override suspend fun refreshBooks(
        subject: String,
        limit: Int?,
        offset: Int?
    ): RefreshResult {
        if (!networkHelper.isNetworkAvailable()) {
            return RefreshResult.Offline
        }
        return try {
            val response = booksApi.getBooksByCategory(subject, limit = limit, offset = offset)
            val existingBooks = localDataSource.getAllBooksByCategory(subject)
            val existingMap = existingBooks.associateBy { it.id }
            val books = response.works.map { work ->
                val existingBook = existingMap[work.key]
                Book(
                    id = work.key,
                    category = subject,
                    title = work.title,
                    author = work.authors.firstOrNull()?.name ?: "Unknown",
                    coverId = work.cover_id,
                    rating = existingBook?.rating ?: generateRandomRating(),
                    price = existingBook?.price ?: generateRandomAmount(),
                    description = existingBook?.description
                )
            }
            localDataSource.insertBooks(books)
            RefreshResult.Success
        } catch (e: Exception) {
            RefreshResult.Error(
                e.message ?: "Something went wrong"
            )
        }
    }

    override suspend fun getBookDetails(id: String): Book? {
        return localDataSource.getBookDetails(id)
    }

    override suspend fun updateBook(id: String): RefreshResult {
        if (!networkHelper.isNetworkAvailable()) {
            return RefreshResult.Offline
        }
        return try {
            val response = booksApi.getBookDetails(id)
            localDataSource.updateBookDescription(id = id, description = response.description)
            RefreshResult.Success
        } catch (e: Exception) {
            RefreshResult.Error(
                e.message ?: "Something went wrong"
            )
        }
    }
}
