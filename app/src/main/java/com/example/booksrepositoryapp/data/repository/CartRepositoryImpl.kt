package com.example.booksrepositoryapp.data.repository

import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.mapper.toFirestore
import com.example.booksrepositoryapp.data.source.remote.firebase.firestore.CartModelFB
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.repository.CartRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CartRepositoryImpl : CartRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun insertCartItem(userId: String, cart: Cart) {
        val cartRef = firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(sanitizeId(cart.bookId))
        
        val snapshot = cartRef.get().await()
        if (snapshot.exists()) {
            val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 0
            cartRef.update("quantity", currentQuantity + 1).await()
        } else {
            cartRef.set(cart.toFirestore()).await()
        }
    }

    override fun getCart(userId: String?): Flow<List<Cart>> = callbackFlow {
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
                    it.toObject(CartModelFB::class.java)?.toDomain()
                } ?: emptyList()
                trySend(cartItems)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun updateCartItem(userId: String, bookId: String, quantity: Int) {
        firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(sanitizeId(bookId))
            .update("quantity", quantity)
            .await()
    }

    override suspend fun deleteCartItem(userId: String, bookId: String) {
        firestore
            .collection("users")
            .document(userId)
            .collection("cart")
            .document(sanitizeId(bookId))
            .delete()
            .await()
    }

    override suspend fun clearCart(userId: String) {
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

    private fun sanitizeId(id: String): String {
        return id.replace("/", "_")
    }
}
