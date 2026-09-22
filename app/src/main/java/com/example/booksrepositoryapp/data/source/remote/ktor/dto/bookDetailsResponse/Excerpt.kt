package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Excerpt(
    val author: AuthorX,
    val comment: String,
    val excerpt: String
)