package com.example.booksrepositoryapp.domain.model

data class Address(
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
    val isSelected: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
