package com.example.booksrepositoryapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val firebaseModule = module {
    single {
        FirebaseFirestore.getInstance()
    }
    single {
        FirebaseAuth.getInstance()
    }
}