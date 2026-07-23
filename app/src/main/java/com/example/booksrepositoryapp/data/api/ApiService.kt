package com.example.booksrepositoryapp.data.api

import com.example.booksrepositoryapp.data.api.models.bookDetailsResponse.BookDetailsResponse
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.SubjectApiResponseModel
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work
import com.example.booksrepositoryapp.ui.books_screen.BooksListState
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