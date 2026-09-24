package com.example.booksrepositoryapp.domain.repository

import com.example.booksrepositoryapp.domain.model.User

interface UserRepository {
    suspend fun createUserProfile(user: User)
    suspend fun getUserProfile(uid: String): User?
    suspend fun updateProfilePicture(uid: String, profilePicture: String?)
}
