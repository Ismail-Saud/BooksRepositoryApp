package com.example.booksrepositoryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBooks (books: List<BookDetailsModel>)

    @Query("UPDATE book_details SET description = :description WHERE workId = :key")
    suspend fun updateBooks (key: String, description: String)

    @Query("SELECT * FROM book_details WHERE category = :category")
    fun getBooksByCategory (category: String) : Flow<List<BookDetailsModel>>

    @Query("SELECT * FROM book_details WHERE workId = :workId")
    suspend fun getBookDetails (workId: String) : BookDetailsModel?
}