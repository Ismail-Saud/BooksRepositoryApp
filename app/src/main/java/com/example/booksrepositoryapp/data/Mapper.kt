package com.example.booksrepositoryapp.data

import com.example.booksrepositoryapp.data.api.models.subjectsApiResponseModels.Work
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel

fun Work.toRoom(category: String): BookDetailsModel {
    return BookDetailsModel(
        workId = key,
        category = category,
        title = title,
        author = authors.firstOrNull()?.name?: "Unknown",
        coverId = cover_id,
        rating = null,
        price = null,
        description = null
    )
}