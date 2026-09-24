package com.example.booksrepositoryapp.data.source.local.roomDataSource

import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.mapper.toEntity
import com.example.booksrepositoryapp.data.source.local.localDataSources.BooksLocalDataSource
import com.example.booksrepositoryapp.data.source.local.room.dao.BooksDao
import com.example.booksrepositoryapp.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomBooksLocalDataSource(
    private val booksDao: BooksDao
) : BooksLocalDataSource {

    override fun getBooksByCategory(
        category: String
    ): Flow<List<Book>> {
        return booksDao
            .getBooksByCategory(category)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun getAllBooksByCategory(
        category: String
    ): List<Book> {
        return booksDao
            .getAllBooksByCategory(category)
            .map { it.toDomain() }
    }

    override suspend fun insertBooks(
        books: List<Book>
    ) {
        booksDao.insertBooks(
            books.map { it.toEntity() }
        )
    }

    override suspend fun getBookDetails(
        id: String
    ): Book? {
        return booksDao
            .getBookDetails(id)
            ?.toDomain()
    }

    override suspend fun updateBookDescription(
        id: String,
        description: String?
    ) {
        booksDao.updateBooks(
            id,
            description ?: ""
        )
    }
}