package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Created(
    val type: String? = null,
    val value: String? = null
)
