package com.example.booksrepositoryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "address_table",
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class AddressModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    var house: String,
    var street: String?,
    var area: String,
    var city: String,
    var postalCode: String?,
    var country: String,
    var fullAddress: String,
    var latitude: Double,
    var longitude: Double,
    var isSelected: Boolean = false
)