package com.example.booksrepositoryapp.data.source.remote.ktor

import com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse.BookDetailsResponse
import com.example.booksrepositoryapp.data.source.remote.ktor.dto.subjectsApiResponseModels.SubjectApiResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService (
    private val client: HttpClient
) {
    suspend fun getBooksByCategory(
        subject: String
    ): SubjectApiResponseModel {
        return client
            .get("subjects/$subject.json")
            .body()
    }

    suspend fun getBookDetails(
        key: String
    ): BookDetailsResponse {
        return client
            .get("$key.json")
            .body()
    }
}