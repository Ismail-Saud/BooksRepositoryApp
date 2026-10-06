package com.example.booksrepositoryapp.data.repository

import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.mapper.toFirestore
import com.example.booksrepositoryapp.data.source.remote.firebase.firestore.AddressModelFB
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AddressRepositoryImpl (
    private val firestore: FirebaseFirestore
) : AddressRepository {
    override fun getAddresses(userId: String): Flow<List<Address>> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    close()
                    return@addSnapshotListener
                }
                val addresses = snapshot?.documents?.mapNotNull {
                    it.toObject(AddressModelFB::class.java)?.copy(id = it.id)?.toDomain()
                } ?: emptyList()
                trySend(addresses)
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addAddress(userId: String, address: Address) {
        if (userId.isEmpty()) return
        val ref = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .document()
        val addressDto = address.toFirestore().copy(id = ref.id)
        ref.set(addressDto).await()
    }

    override suspend fun updateAddress(userId: String, address: Address) {
        if (userId.isEmpty()) return
        firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .document(address.id)
            .set(address.toFirestore())
            .await()
    }

    override suspend fun deleteAddress(userId: String, addressId: String) {
        if (userId.isEmpty()) return
        firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .document(addressId)
            .delete()
            .await()
    }

    override suspend fun deleteAllAddresses(userId: String) {
        if (userId.isEmpty()) return
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

    override suspend fun updateSelectedAddress(userId: String, addressId: String) {
        if (userId.isEmpty()) return
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

    override fun getSelectedAddress(userId: String): Flow<Address?> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .whereEqualTo("isSelected", true)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    close()
                    return@addSnapshotListener
                }
                val address = snapshot
                    ?.documents
                    ?.firstOrNull()
                    ?.let { document ->
                        document
                            .toObject(AddressModelFB::class.java)
                            ?.copy(id = document.id)
                            ?.toDomain()
                    }
                trySend(address)
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun getAddressCount(userId: String): Flow<Int> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(0)
            close()
            return@callbackFlow
        }
        val listener = firestore
            .collection("users")
            .document(userId)
            .collection("addresses")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(0)
                    close()
                    return@addSnapshotListener
                }
                trySend(snapshot?.size() ?: 0)
            }
        awaitClose {
            listener.remove()
        }
    }
}