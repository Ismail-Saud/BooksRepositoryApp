package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.domain.model.Book
import kotlin.test.Test
import kotlin.test.assertEquals

class BooksMapperTest {
    private val book = Book(
        id = "/works/OL1W",
        category = "fantasy",
        title = "The Hobbit",
        author = "J.R.R. Tolkien",
        coverId = 123,
        rating = 4.8,
        price = 19.99,
        description = "A fantasy novel."
    )

    @Test
    fun bookMapsToRoomEntity() {
        assertEquals(
            BookDetailsModel(
                workId = book.id,
                category = book.category,
                title = book.title,
                author = book.author,
                coverId = book.coverId,
                rating = book.rating,
                price = book.price,
                description = book.description
            ),
            book.toEntity()
        )
    }

    @Test
    fun roomEntityMapsBackToDomainBook() {
        val entity = book.toEntity()

        assertEquals(book, entity.toDomain())
    }
}
