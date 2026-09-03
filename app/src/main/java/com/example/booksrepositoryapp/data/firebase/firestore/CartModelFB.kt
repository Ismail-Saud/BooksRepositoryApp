package com.example.booksrepositoryapp.data.firebase.firestore

data class CartModelFB (
    val workId: String = "",
    val title: String = "",
    val author: String = "",
    val price: Double = 0.0,
    val coverId: Int = 0,
    val category: String = "",
    val quantity: Int = 1
)