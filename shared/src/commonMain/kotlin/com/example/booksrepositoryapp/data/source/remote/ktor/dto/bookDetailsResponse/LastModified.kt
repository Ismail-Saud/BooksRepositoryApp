package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class LastModified(
    val type: String? = null,
    val value: String? = null
)
