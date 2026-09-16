package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.repository.BooksRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.BooksRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import com.example.booksrepositoryapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {

    @Binds
    @Singleton
    abstract fun provideUserRepository (
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun providesAddressRepository (
        impl: AddressRepositoryImpl
    ): AddressRepository

    @Binds
    @Singleton
    abstract fun providesCartRepository (
        impl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun providesBooksRepository (
        impl: BooksRepositoryImpl
    ): BooksRepository
}