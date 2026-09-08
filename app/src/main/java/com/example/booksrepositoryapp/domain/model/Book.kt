package com.example.booksrepositoryapp.domain.model

data class Book(
    val id: String,
    val category: String,
    val title: String,
    val author: String,
    val coverId: Int,
    val rating: Double?,
    val price: Double?,
    val description: String?
)
