package com.example.booksrepositoryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel (
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    val password: String
)