package com.example.booksrepositoryapp.data.repository

import android.content.Context
import android.database.sqlite.SQLiteException
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.source.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.source.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.data.source.remote.retrofit.RetrofitInstance
import com.example.booksrepositoryapp.data.util.generateRandomAmount
import com.example.booksrepositoryapp.data.util.generateRandomRating
import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import com.example.booksrepositoryapp.helper.networkHelper.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class BooksRepositoryImpl(context: Context) : BooksRepository {
    private val booksApi = RetrofitInstance.api
    private val dao = DatabaseInstance.getDatabase(context).BooksDao()
    private val networkHelper = NetworkHelper(context)

    override fun getBooks(category: String): Flow<List<Book>> {
        return dao.getBooksByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun refreshBooks(subject: String): RefreshResult {
        if (!networkHelper.isNetworkAvailable()) {
            return RefreshResult.Offline
        }
        return try {
            val response = booksApi.getBooksByCategory(subject)
            val existingBooks = dao.getAllBooksByCategory(subject)
            val existingMap = existingBooks.associateBy { it.workId }
            val books = response.works.map { work ->
                val existingBook = existingMap[work.key]
                BookDetailsModel(
                    workId = work.key,
                    category = subject,
                    title = work.title,
                    author = work.authors.firstOrNull()?.name ?: "Unknown",
                    coverId = work.cover_id,
                    rating = existingBook?.rating ?: generateRandomRating(),
                    price = existingBook?.price ?: generateRandomAmount(),
                    description = existingBook?.description
                )
            }
            dao.insertBooks(books)
            RefreshResult.Success
        } catch (e: IOException) {
            Log.e("Offline", "${e.message}")
            RefreshResult.Error("No internet connection ${e.message}")
        } catch (e: HttpException) {
            RefreshResult.Error("Server error: ${e.message}")
        } catch (e: SQLiteException) {
            RefreshResult.Error("Database error: ${e.message}")
        } catch (e: Exception) {
            RefreshResult.Error("Something went wrong: ${e.message}")
        }
    }

    override suspend fun getBookDetails(id: String): Book? {
        return dao.getBookDetails(id)?.toDomain()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun updateBook(id: String): RefreshResult {
        if (!networkHelper.isNetworkAvailable()) {
            return RefreshResult.Offline
        }
        return try {
            val response = booksApi.getBookDetails(id)
            dao.updateBooks(id, response.description)
            RefreshResult.Success
        } catch (e: IOException) {
            RefreshResult.Error("No internet connection ${e.message}")
        } catch (e: HttpException) {
            RefreshResult.Error("Server error: ${e.message}")
        } catch (e: SQLiteException) {
            RefreshResult.Error("Database error: ${e.message}")
        } catch (e: Exception) {
            RefreshResult.Error("Something went wrong: ${e.message}")
        }
    }
}
