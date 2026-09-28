package com.example.booksrepositoryapp.data.source.local.roomDataSource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.booksrepositoryapp.data.source.local.room.AppDatabase
import com.example.booksrepositoryapp.domain.model.Book
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class RoomBooksLocalDataSourceTest {
    private lateinit var database: AppDatabase
    private lateinit var dataSource: RoomBooksLocalDataSource

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dataSource = RoomBooksLocalDataSource(database.BooksDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertsAndUpdatesBookDetails() = runTest {
        val book = Book(
            id = "/works/OL1W",
            category = "fantasy",
            title = "The Hobbit",
            author = "J.R.R. Tolkien",
            coverId = 123,
            rating = 4.8,
            price = 19.99,
            description = null
        )

        dataSource.insertBooks(listOf(book))
        dataSource.updateBookDescription(book.id, "A fantasy novel.")

        assertEquals(
            book.copy(description = "A fantasy novel."),
            dataSource.getBookDetails(book.id)
        )
    }
}
