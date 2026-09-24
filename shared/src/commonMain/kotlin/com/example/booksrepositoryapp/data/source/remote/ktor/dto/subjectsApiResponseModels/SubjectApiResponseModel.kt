package com.example.booksrepositoryapp.data.source.remote.ktor.dto.subjectsApiResponseModels

import kotlinx.serialization.Serializable

@Serializable
data class SubjectApiResponseModel(
    val works: List<Work> = emptyList()
)
