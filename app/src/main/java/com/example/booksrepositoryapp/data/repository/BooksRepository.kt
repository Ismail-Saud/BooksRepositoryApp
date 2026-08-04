package com.example.booksrepositoryapp.data.repository

import android.content.Context
import android.database.sqlite.SQLiteException
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.booksrepositoryapp.data.api.RetrofitInstance
import com.example.booksrepositoryapp.data.api.models.bookDetailsResponse.BookDetailsResponse
import com.example.booksrepositoryapp.data.api.networkHelper.NetworkHelper
import com.example.booksrepositoryapp.data.api.refreshResult.RefreshResult
import com.example.booksrepositoryapp.data.local.generateRandomAmount
import com.example.booksrepositoryapp.data.local.generateRandomRating
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class BooksRepository(context: Context) {
    val booksApi = RetrofitInstance.api
    val dao = DatabaseInstance.getDatabase(context).BooksDao()
    private val networkHelper = NetworkHelper(context)

    fun getBooks (category: String) : Flow<List<BookDetailsModel>> {
        return dao.getBooksByCategory(category)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun refreshBooks(subject: String): RefreshResult {
        if (!networkHelper.isNetworkAvailable()) {
            return RefreshResult.Offline
        }
        return try {
            val response =  booksApi.getBooksByCategory(subject)
            val books = response.works.map { work ->
                val existingBook = dao.getBookDetails(work.key)
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
            Log.e("Offline","${e.message}")
            RefreshResult.Error("No internet connection ${e.message}")
        } catch (e: HttpException) {
            RefreshResult.Error("Server error: ${e.message}")
        } catch (e: SQLiteException) {
            RefreshResult.Error("Database error: ${e.message}")
        } catch (e: Exception) {
            RefreshResult.Error("Something went wrong: ${e.message}")
        }
    }

    suspend fun getBooksApi (subject: String) {
        booksApi.getBooksByCategory(subject)
    }

    suspend fun getBookDetails(key: String) : BookDetailsModel? {
        return dao.getBookDetails(key)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun updateBook (key: String): RefreshResult {
        if (!networkHelper.isNetworkAvailable()) {
            return RefreshResult.Offline
        }
        return try {
            val response = booksApi.getBookDetails(key)
            dao.updateBooks(key, response.description)
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