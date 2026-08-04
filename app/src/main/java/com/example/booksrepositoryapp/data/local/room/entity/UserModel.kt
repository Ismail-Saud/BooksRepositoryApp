package com.example.booksrepositoryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class UserModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val username: String,
    val email: String,
    val password: String,
    val profilePicture: String? = null
)