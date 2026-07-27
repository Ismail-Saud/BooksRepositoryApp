package com.example.booksrepositoryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart",
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookDetailsModel::class,
            parentColumns = ["workId"],
            childColumns = ["workId"],
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [
        Index("id"),
        Index("workId")
    ]
)
data class CartModel (
    @PrimaryKey(autoGenerate = true)
    val cartId: Int = 0,
    val id: Int,
    val workId: String,
    val quantity: Int = 1
)
