package com.example.booksrepositoryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booksrepositoryapp.data.local.room.dao.BooksDao
import com.example.booksrepositoryapp.data.local.room.dao.CartDao
import com.example.booksrepositoryapp.data.local.room.dao.UserDao
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.data.local.room.entity.CartModel
import com.example.booksrepositoryapp.data.local.room.entity.UserModel

@Database(
    entities = [UserModel::class, BookDetailsModel::class, CartModel::class],
    version = 6,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun BooksDao() : BooksDao
    abstract fun CartDao() : CartDao
}