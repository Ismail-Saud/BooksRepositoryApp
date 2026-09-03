package com.example.booksrepositoryapp.data.firebase.firestore

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class AddressModelFB(
    val id: String = "",
    val house: String = "",
    val street: String = "",
    val area: String = "",
    val city: String = "",
    val postalCode: String = "",
    val country: String = "",
    val fullAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    @get:PropertyName("isSelected")
    @PropertyName("isSelected")
    val isSelected: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),

    @get:Exclude
    val isFetchingLocation: Boolean = false,
    @get:Exclude
    val isSaving: Boolean = false
)
