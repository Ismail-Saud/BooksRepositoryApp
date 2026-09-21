package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.local.room.AppDatabase
import com.example.booksrepositoryapp.data.source.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.source.local.room.dao.BooksDao
import org.koin.dsl.module

val roomModule = module {
    single {
        DatabaseInstance.getDatabase(get())
    }
    single<BooksDao> {
        get<AppDatabase>().BooksDao()
    }
}