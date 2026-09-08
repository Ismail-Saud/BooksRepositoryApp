package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.domain.model.Book

fun BookDetailsModel.toDomain(): Book {
    return Book(
        id = workId,
        category = category,
        title = title,
        author = author,
        coverId = coverId,
        rating = rating,
        price = price,
        description = description
    )
}

fun Book.toEntity(): BookDetailsModel {
    return BookDetailsModel(
        workId = id,
        category = category,
        title = title,
        author = author,
        coverId = coverId,
        rating = rating,
        price = price,
        description = description
    )
}
