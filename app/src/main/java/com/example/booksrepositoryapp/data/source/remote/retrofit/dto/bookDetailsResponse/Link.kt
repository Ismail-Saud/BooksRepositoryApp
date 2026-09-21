package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val title: String,
    val type: TypeXX,
    val url: String
)