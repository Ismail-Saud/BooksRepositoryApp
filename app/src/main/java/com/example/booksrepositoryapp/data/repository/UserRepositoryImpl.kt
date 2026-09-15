package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.mapper.toProfile
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.domain.model.User
import com.example.booksrepositoryapp.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(context: Context? = null) : UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun createUserProfile(user: User) {
        firestore
            .collection("users")
            .document(user.id)
            .set(user.toProfile())
            .await()
    }

    override suspend fun getUserProfile(uid: String): User? {
        return firestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserProfile::class.java)?.toDomain()
    }

    override suspend fun updateProfilePicture(uid: String, profilePicture: String?) {
        firestore
            .collection("users")
            .document(uid)
            .update("profilePicture", profilePicture)
            .await()
    }
}
