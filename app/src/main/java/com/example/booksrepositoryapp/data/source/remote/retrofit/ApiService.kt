package com.example.booksrepositoryapp.data.source.remote.retrofit

import com.example.booksrepositoryapp.data.source.remote.retrofit.dto.bookDetailsResponse.BookDetailsResponse
import com.example.booksrepositoryapp.data.source.remote.retrofit.dto.subjectsApiResponseModels.SubjectApiResponseModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("subjects/{subject}.json")
    suspend fun getBooksByCategory(
        @Path("subject") subject: String
    ): SubjectApiResponseModel

    @GET("{key}.json")
    suspend fun getBookDetails(
        @Path("key") key: String
    ): BookDetailsResponse
}