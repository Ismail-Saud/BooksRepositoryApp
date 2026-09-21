package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.subjectsApiResponseModels

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val name: String = "Unknown"
)
