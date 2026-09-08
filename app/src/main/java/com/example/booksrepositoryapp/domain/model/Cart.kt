package com.example.booksrepositoryapp.domain.model

data class Cart(
    val bookId: String,
    val title: String,
    val author: String,
    val price: Double,
    val coverId: Int,
    val category: String,
    val quantity: Int
)
