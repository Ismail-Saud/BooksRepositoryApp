package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.firebase.firestore.CartModelFB
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CartRepository(context: Context) {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun insertCartItem(userId: String, cartItem: CartModelFB) {
        val cartRef = firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(sanitizeId(cartItem.workId))
        val snapshot = cartRef.get().await()
        if (snapshot.exists()) {
            val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 0
            cartRef.update(
                "quantity",
                currentQuantity + 1
            ).await()
        } else {
            cartRef.set(cartItem).await()
        }
    }

    fun getCart(userId: String?): Flow<List<CartModelFB>> = callbackFlow {
        val listener = firestore
            .collection("users")
            .document(userId.toString())
            .collection("cart")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val cartItems = snapshot?.documents?.mapNotNull {
                    it.toObject(CartModelFB::class.java)
                } ?: emptyList()
                trySend(cartItems)
            }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun updateCartItem(userId: String, bookId: String, quantity: Int) {
        firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(sanitizeId(bookId))
            .update("quantity", quantity)
            .await()
    }

    suspend fun deleteCartItem(userId: String, bookId: String) {
        firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(sanitizeId(bookId))
            .delete()
            .await()
    }

    private fun sanitizeId(id: String): String {
        return id.replace("/", "_")
    }

    suspend fun clearCart(userId: String) {
        val cartItems = firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .await()
        val batch = firestore.batch()
        for (document in cartItems.documents) {
            batch.delete(document.reference)
        }
        batch.commit().await()
    }

//    suspend fun insertCartItem (userId: String, bookId: String) {
//        val cartItem = dao.getCartEntity(userId.toInt(), bookId)
//        if (cartItem == null) {
//            dao.insert(
//                CartModel(
//                    id = userId.toInt(),
//                    workId = bookId,
//                    quantity = 1
//                )
//            )
//        }
//        else {
//            dao.update(
//                cartItem.copy(
//                    quantity = cartItem.quantity + 1
//                )
//            )
//        }
//     }


//    fun getCart (id: Int): Flow<List<CartItem>> {
//        return dao.getCart(id)
//    }

//    suspend fun updateCartItem (cart: CartModel) {
//        dao.update(cart)
//    }

//    suspend fun deleteCartItem (cart: CartModel) {
//        dao.delete(cart)
//    }

//    suspend fun clearCart() {
//        dao.clearCart()
//    }

}