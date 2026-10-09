package com.example.booksrepositoryapp.data.source.local.room.entity

import androidx.room.Entity

@Entity(
    tableName = "book_details",
    primaryKeys = ["workId", "category"]
)
data class BookDetailsModel(
    val workId: String,
    val category: String,
    val title: String,
    val author: String,
    val coverId: Int,
    val rating: Double?,
    val price: Double?,
    val description: String?
)
