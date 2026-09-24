package com.example.booksrepositoryapp.domain.repository

import com.example.booksrepositoryapp.domain.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun insertCartItem(userId: String, cart: Cart)
    fun getCart(userId: String?): Flow<List<Cart>>
    suspend fun updateCartItem(userId: String, bookId: String, quantity: Int)
    suspend fun deleteCartItem(userId: String, bookId: String)
    suspend fun clearCart(userId: String)
}
