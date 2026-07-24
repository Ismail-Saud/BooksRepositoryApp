package com.example.booksrepositoryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_details")
data class BookDetailsModel(
    @PrimaryKey
    val workId: String,
    val category: String,
    val title: String,
    val author: String,
    val coverId: Int,
    val rating: Double?,
    val price: Double?,
    val description: String?
)
