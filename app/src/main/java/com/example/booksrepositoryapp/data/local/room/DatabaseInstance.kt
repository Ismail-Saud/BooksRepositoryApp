package com.example.booksrepositoryapp.data.local.room

import android.content.Context
import androidx.room.Room


object DatabaseInstance {
    @Volatile
    var INSTANCE: AppDatabase ?= null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "UserDatabase"
            ).fallbackToDestructiveMigration(true).build()

            INSTANCE = instance
            instance
        }
    }
}