package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val author: AuthorX,
    val type: TypeXX
)