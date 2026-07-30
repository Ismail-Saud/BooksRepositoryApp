package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.entity.CartModel
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepository(context: Context) {

    val dao = DatabaseInstance.getDatabase(context).CartDao()

    suspend fun insertCartItem (userId: Int, bookId: String) {
        val cartItem = dao.getCartEntity(userId, bookId)
        if (cartItem == null) {
            dao.insert(
                CartModel(
                    id = userId,
                    workId = bookId,
                    quantity = 1
                )
            )
        }
        else {
            dao.update(
                cartItem.copy(
                    quantity = cartItem.quantity + 1
                )
            )
        }
//        dao.insert(cart)
    }

    fun getCart (id: Int): Flow<List<CartItem>> {
        return dao.getCart(id)
    }

    suspend fun updateCartItem (cart: CartModel) {
        dao.update(cart)
    }

    suspend fun deleteCartItem (cart: CartModel) {
        dao.delete(cart)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

}