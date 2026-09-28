package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val author: AuthorX? = null,
    val type: TypeXX? = null
)
