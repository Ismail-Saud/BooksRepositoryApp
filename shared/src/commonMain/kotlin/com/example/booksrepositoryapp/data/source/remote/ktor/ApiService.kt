package com.example.booksrepositoryapp.data.source.remote.ktor

import com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse.BookDetailsResponse
import com.example.booksrepositoryapp.data.source.remote.ktor.dto.subjectsApiResponseModels.SubjectApiResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService(
    private val client: HttpClient
) {
    suspend fun getBooksByCategory(
        subject: String,
        limit: Int? = null,
        offset: Int? = null
    ): SubjectApiResponseModel {
        return client
            .get("subjects/$subject.json") {
                if (limit != null) parameter("limit", limit)
                if (offset != null) parameter("offset", offset)
            }
            .body()
    }

    suspend fun getBookDetails(key: String): BookDetailsResponse {
        val path = if (key.endsWith(".json")) key else "$key.json"
        val formattedKey = if (path.startsWith("/")) path.removePrefix("/") else path
        return client
            .get(formattedKey)
            .body()
    }
}
