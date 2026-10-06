package com.example.booksrepositoryapp.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.example.booksrepositoryapp.data.mapper.toDomain
import com.example.booksrepositoryapp.data.mapper.toProfile
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.domain.model.User
import com.example.booksrepositoryapp.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException

class UserRepositoryImpl (
    private val firestore: FirebaseFirestore,
    private val context: Context
) : UserRepository {
    override suspend fun createUserProfile(user: User) {
        if (user.id.isEmpty()) return
        firestore
            .collection("users")
            .document(user.id)
            .set(user.toProfile())
            .await()
    }

    override suspend fun getUserProfile(uid: String): User? {
        if (uid.isEmpty()) return null
        return firestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserProfile::class.java)?.toDomain()
    }

    override suspend fun updateProfilePicture(uid: String, profilePicture: String?) {
        if (uid.isEmpty()) return
        firestore
            .collection("users")
            .document(uid)
            .update("profilePicture", profilePicture)
            .await()
    }

    override suspend fun saveProfilePicture(uid: String, imageUri: String, oldFileName: String?): String {
        oldFileName?.let { deleteLocalProfilePicture(it) }
        val fileName = "profile_${uid}_${System.currentTimeMillis()}.jpg"
        val destFile = File(context.filesDir, fileName)
        val uri = imageUri.toUri()
        context.contentResolver.openInputStream(uri)?.use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: throw IOException("Could not open URI: $imageUri")
        updateProfilePicture(uid, fileName)
        return fileName
    }

    override suspend fun deleteLocalProfilePicture(fileName: String) {
        try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                file.delete()
            }
        } catch (_: Exception) { }
    }
}
