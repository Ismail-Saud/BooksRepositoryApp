package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.local.localDataSources.BooksLocalDataSource
import com.example.booksrepositoryapp.data.source.local.room.AppDatabase
import com.example.booksrepositoryapp.data.source.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.source.local.room.dao.BooksDao
import com.example.booksrepositoryapp.data.source.local.roomDataSource.RoomBooksLocalDataSource
import org.koin.dsl.module

val roomModule = module {
    single<AppDatabase> {
        DatabaseInstance.getDatabase(get())
    }
    single<BooksDao> {
        get<AppDatabase>().BooksDao()
    }
    single<BooksLocalDataSource> {
        RoomBooksLocalDataSource(
            booksDao = get()
        )
    }
}