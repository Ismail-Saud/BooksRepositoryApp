package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.api.RetrofitInstance
import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.SubjectApiResponseModel

class BooksRepository() {
    val booksApi = RetrofitInstance.api

    suspend fun getBooksByCategory(subject: String) : SubjectApiResponseModel {
        return booksApi.getBooksByCategory(subject)
    }

}