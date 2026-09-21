package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Created(
    val type: String,
    val value: String
)