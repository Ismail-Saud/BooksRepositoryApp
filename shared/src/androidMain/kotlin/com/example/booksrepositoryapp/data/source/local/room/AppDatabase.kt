package com.example.booksrepositoryapp.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booksrepositoryapp.data.source.local.room.dao.BooksDao
import com.example.booksrepositoryapp.data.source.local.room.entity.BookDetailsModel

@Database(
    entities = [BookDetailsModel::class],
    version = 9,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun BooksDao() : BooksDao
}