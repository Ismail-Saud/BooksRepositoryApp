package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.firebase.firestore.AddressModelFB
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AddressRepository(context: Context) {
    private val firestore = FirebaseFirestore.getInstance()

    fun getAddresses(userId: String): Flow<List<AddressModelFB>> = callbackFlow {
        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val addresses = snapshot?.documents?.mapNotNull {
                    it.toObject(AddressModelFB::class.java)?.copy(id = it.id)
                } ?: emptyList()
                trySend(addresses)
            }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addAddress(userId: String, address: AddressModelFB) {
        val ref = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .document()
        ref.set(address.copy(id = ref.id)).await()
    }

    suspend fun updateAddress(userId: String, address: AddressModelFB) {
        firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .document(address.id)
            .set(address)
            .await()
    }

    suspend fun deleteAddress(userId: String, addressId: String) {
        firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .document(addressId)
            .delete()
            .await()
    }

    suspend fun deleteAllAddresses(userId: String) {
        val addresses = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .get()
            .await()
        val batch = firestore.batch()
        for (document in addresses.documents) {
            batch.delete(document.reference)
        }
        batch.commit().await()
    }

    suspend fun updateSelectedAddress(userId: String, addressId: String) {
        val addressesRef = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
        
        val snapshot = addressesRef.get().await()
        val batch = firestore.batch()
        for (doc in snapshot.documents) {
            batch.update(doc.reference, "isSelected", doc.id == addressId)
        }
        batch.commit().await()
    }

    fun getSelectedAddress(userId: String): Flow<AddressModelFB?> = callbackFlow {
        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .whereEqualTo("isSelected", true)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val address = snapshot?.documents?.firstOrNull()?.toObject(AddressModelFB::class.java)
                trySend(address)
            }
        awaitClose {
            listener.remove()
        }
    }

    fun getAddressCount(userId: String): Flow<Int> = callbackFlow {
        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0)
            }
        awaitClose {
            listener.remove()
        }
    }
}
