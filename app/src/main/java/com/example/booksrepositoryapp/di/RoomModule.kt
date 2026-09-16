package com.example.booksrepositoryapp.di

import android.content.Context
import com.example.booksrepositoryapp.data.source.local.room.AppDatabase
import com.example.booksrepositoryapp.data.source.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.source.local.room.dao.BooksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun providesBooksDao(
        database: AppDatabase
    ): BooksDao {
        return database.BooksDao()
    }

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return DatabaseInstance.getDatabase(context)
    }
}