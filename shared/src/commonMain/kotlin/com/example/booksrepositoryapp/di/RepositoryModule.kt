package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.repository.BooksRepositoryImpl
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BooksRepository> {
        BooksRepositoryImpl(
            booksApi = get(),
            localDataSource = get(),
            networkHelper = get()
        )
    }
}