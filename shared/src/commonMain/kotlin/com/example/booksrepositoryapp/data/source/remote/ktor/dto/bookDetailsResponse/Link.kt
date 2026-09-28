package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val title: String? = null,
    val type: TypeXX? = null,
    val url: String? = null
)
