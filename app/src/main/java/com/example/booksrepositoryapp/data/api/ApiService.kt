package com.example.booksrepositoryapp.data.api

import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.SubjectApiResponseModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("subjects/{subject}.json")
    suspend fun getBooksByCategory(
        @Path("subject") subject: String
    ): SubjectApiResponseModel
}