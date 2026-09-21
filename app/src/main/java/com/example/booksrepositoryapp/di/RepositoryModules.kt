package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.repository.BooksRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import com.example.booksrepositoryapp.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModules = module {
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<AddressRepository> {
        AddressRepositoryImpl(get())
    }
    single<CartRepository> {
        CartRepositoryImpl(get())
    }
    single<BooksRepository> {
        BooksRepositoryImpl(get(), get(), get())
    }
    single<AuthRepository> {
        AuthRepository(get())
    }
}