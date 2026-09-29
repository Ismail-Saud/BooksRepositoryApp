package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.repository.UserRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.FirebaseAuthRepository
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import com.example.booksrepositoryapp.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { FirebaseAuthRepository(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), androidContext()) }
    single<AddressRepository> { AddressRepositoryImpl(get()) }
    single<CartRepository> {
        CartRepositoryImpl(get()) }
}
