package com.example.booksrepositoryapp.data.source.remote.ktor.dto.subjectsApiResponseModels

import kotlinx.serialization.Serializable

@Serializable
data class Work(
    val authors: List<Author> = emptyList(),
    val cover_id: Int = 0,
    val key: String = "",
    val title: String = ""
)
