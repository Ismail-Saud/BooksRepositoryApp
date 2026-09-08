package com.example.booksrepositoryapp.domain.model

data class User(
    val id: String, // UID for Firebase, or String version of Room ID
    val username: String,
    val email: String,
    val profilePicture: String? = null
)
