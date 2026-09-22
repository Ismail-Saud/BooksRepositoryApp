package com.example.booksrepositoryapp.data.source.remote.ktor.dto.subjectsApiResponseModels

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val key: String = "",
    val name: String = "Unknown"
)
