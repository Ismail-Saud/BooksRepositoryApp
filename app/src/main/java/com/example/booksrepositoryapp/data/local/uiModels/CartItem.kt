package com.example.booksrepositoryapp.data.local.uiModels

data class CartItem(
    val cartId: Int,
    val bookId: String,
    val userId: Int,
    val title: String,
    val category: String,
    val author: String,
    val price: Double,
    val coverId: Int,
    val quantity: Int
)
