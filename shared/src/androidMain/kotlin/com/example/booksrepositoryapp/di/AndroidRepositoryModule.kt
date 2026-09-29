package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import com.example.booksrepositoryapp.domain.repository.UserRepository
import org.koin.dsl.module

val androidRepositoryModule = module {
    single<AddressRepository> {
        AddressRepositoryImpl(
            firestore = get()
        )
    }

    single<CartRepository> {
        CartRepositoryImpl(
            firestore = get()
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            firestore = get(),
            context = get()
        )
    }
}