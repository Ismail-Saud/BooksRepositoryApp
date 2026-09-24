package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.FirebaseAuthRepository
import org.koin.dsl.module

val firebaseModule = module {
    single<AuthRepository> {
        FirebaseAuthRepository(get())
    }
}