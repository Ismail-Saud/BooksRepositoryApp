package com.example.booksrepositoryapp.data.source.remote.firebase.authentication

interface AuthRepository {
    fun getCurrentUserId(): String?
    suspend fun createUser(
        username: String,
        email: String,
        password: String
    ): Result<String>
    suspend fun login(
        email: String,
        password: String
    ): Result<String>
    fun logout()
}