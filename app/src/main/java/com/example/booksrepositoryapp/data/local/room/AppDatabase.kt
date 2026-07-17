package com.example.booksrepositoryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booksrepositoryapp.data.local.room.dao.UserDao
import com.example.booksrepositoryapp.data.local.room.entity.UserModel

@Database(
    entities = [UserModel::class],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun UserDao(): UserDao
}