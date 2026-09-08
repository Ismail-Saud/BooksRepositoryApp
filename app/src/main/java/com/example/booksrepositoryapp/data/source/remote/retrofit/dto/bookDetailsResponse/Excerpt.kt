package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.bookDetailsResponse

data class Excerpt(
    val author: AuthorX,
    val comment: String,
    val excerpt: String
)