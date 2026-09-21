package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.subjectsApiResponseModels

import kotlinx.serialization.Serializable

@Serializable
data class Work(
    val key: String,
    val title: String,
    val authors: List<Author> = emptyList(),
    val cover_id: Int? = null
)
