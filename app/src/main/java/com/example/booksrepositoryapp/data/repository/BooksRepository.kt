package com.example.booksrepositoryapp.data.repository

import com.example.booksrepositoryapp.data.api.RetrofitInstance
import com.example.booksrepositoryapp.data.api.models.bookDetailsResponse.BookDetailsResponse
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.SubjectApiResponseModel
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work

class BooksRepository {
    val booksApi = RetrofitInstance.api

    suspend fun getBooksByCategory(subject: String) : SubjectApiResponseModel {
        return booksApi.getBooksByCategory(subject)
    }

    suspend fun getBookDetails(key: String) : BookDetailsResponse {
        return booksApi.getBookDetails(key)
    }

}