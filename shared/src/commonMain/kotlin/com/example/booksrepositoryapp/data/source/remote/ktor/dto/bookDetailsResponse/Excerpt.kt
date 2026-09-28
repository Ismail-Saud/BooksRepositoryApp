package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Excerpt(
    val author: AuthorX? = null,
    val comment: String? = null,
    val excerpt: String? = null
)
