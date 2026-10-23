package com.example.booksrepositoryapp.data.repository

import com.example.booksrepositoryapp.data.source.local.localDataSources.BooksLocalDataSource
import com.example.booksrepositoryapp.data.source.remote.ktor.ApiService
import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.manager.networkManager.NetworkManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeBooksLocalDataSource : BooksLocalDataSource {
    val localBooks = mutableListOf<Book>()
    val insertedBooks = mutableListOf<Book>()

    override fun getBooksByCategory(category: String): Flow<List<Book>> {
        return flowOf(localBooks.filter { it.category == category })
    }

    override suspend fun getAllBooksByCategory(category: String): List<Book> {
        return localBooks.filter { it.category == category }
    }

    override suspend fun insertBooks(books: List<Book>) {
        insertedBooks.clear()
        insertedBooks.addAll(books)
        localBooks.removeAll { existing -> books.any { it.id == existing.id } }
        localBooks.addAll(books)
    }

    override suspend fun getBookDetails(id: String): Book? {
        return localBooks.find { it.id == id }
    }

    override suspend fun updateBookDescription(id: String, description: String?) {
        val index = localBooks.indexOfFirst { it.id == id }
        if (index != -1) {
            val book = localBooks[index]
            localBooks[index] = book.copy(description = description)
        }
    }
}

class FakeNetworkManager(var isAvailable: Boolean = true) : NetworkManager {
    override fun isNetworkAvailable(): Boolean = isAvailable
}

class BooksRepositoryTest {

    private fun createApiService(jsonResponse: String, status: HttpStatusCode = HttpStatusCode.OK): ApiService {
        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }
        return ApiService(client)
    }

    @Test
    fun testOfflineRefresh() = runTest {
        val fakeLocal = FakeBooksLocalDataSource()
        val fakeNetwork = FakeNetworkManager(isAvailable = false)
        val apiService = createApiService("{}")
        val repository = BooksRepositoryImpl(apiService, fakeLocal, fakeNetwork)

        val result = repository.refreshBooks("fantasy")
        assertEquals(RefreshResult.Offline, result)
    }

    @Test
    fun testGetBooksReadsTheRequestedCategoryFromLocalDataSource() = runTest {
        val local = FakeBooksLocalDataSource()
        val fantasyBook = Book(
            id = "/works/OL400W", category = "fantasy", title = "Dune", author = "Frank Herbert",
            coverId = 1, rating = 4.5, price = 20.0, description = null
        )
        local.localBooks += fantasyBook
        local.localBooks += fantasyBook.copy(id = "/works/OL401W", category = "fiction")
        val repository = BooksRepositoryImpl(createApiService("{}"), local, FakeNetworkManager())

        assertEquals(listOf(fantasyBook), repository.getBooks("fantasy").first())
    }

    @Test
    fun testGetBookDetailsReturnsTheCachedBook() = runTest {
        val local = FakeBooksLocalDataSource()
        val book = Book(
            id = "/works/OL402W", category = "fiction", title = "Dune", author = "Frank Herbert",
            coverId = 1, rating = null, price = null, description = "Cached description"
        )
        local.localBooks += book
        val repository = BooksRepositoryImpl(createApiService("{}"), local, FakeNetworkManager())

        assertEquals(book, repository.getBookDetails(book.id))
        assertEquals(null, repository.getBookDetails("missing"))
    }

    @Test
    fun testRefreshFailure() = runTest {
        val fakeLocal = FakeBooksLocalDataSource()
        val fakeNetwork = FakeNetworkManager(isAvailable = true)
        val mockEngine = MockEngine { throw Exception("Server error") }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
        val apiService = ApiService(client)
        val repository = BooksRepositoryImpl(apiService, fakeLocal, fakeNetwork)

        val result = repository.refreshBooks("fantasy")
        assertTrue(result is RefreshResult.Error)
    }

    @Test
    fun testPreservingLocalPriceAndRating() = runTest {
        val fakeLocal = FakeBooksLocalDataSource()
        val fakeNetwork = FakeNetworkManager(isAvailable = true)

        val existingBook = Book(
            id = "/works/OL100W",
            category = "fantasy",
            title = "Old Title",
            author = "Old Author",
            coverId = 123,
            rating = 4.8,
            price = 19.99,
            description = "Local desc"
        )
        fakeLocal.localBooks.add(existingBook)

        val responseJson = """
            {
              "works": [
                {
                  "key": "/works/OL100W",
                  "title": "New Title",
                  "cover_id": 456,
                  "authors": [{ "key": "/authors/A1", "name": "New Author" }]
                }
              ]
            }
        """.trimIndent()

        val apiService = createApiService(responseJson)
        val repository = BooksRepositoryImpl(apiService, fakeLocal, fakeNetwork)

        val result = repository.refreshBooks("fantasy")
        assertEquals(RefreshResult.Success, result)

        val refreshedBook = fakeLocal.insertedBooks.first()
        assertEquals(4.8, refreshedBook.rating)
        assertEquals(19.99, refreshedBook.price)
        assertEquals("New Title", refreshedBook.title)
    }

    @Test
    fun testInsertingRefreshedBooksIntoLocalDataSource() = runTest {
        val fakeLocal = FakeBooksLocalDataSource()
        val fakeNetwork = FakeNetworkManager(isAvailable = true)

        val responseJson = """
            {
              "works": [
                {
                  "key": "/works/OL200W",
                  "title": "1984",
                  "cover_id": 789,
                  "authors": [{ "key": "/authors/A2", "name": "George Orwell" }]
                }
              ]
            }
        """.trimIndent()

        val apiService = createApiService(responseJson)
        val repository = BooksRepositoryImpl(apiService, fakeLocal, fakeNetwork)

        val result = repository.refreshBooks("fiction")
        assertEquals(RefreshResult.Success, result)

        assertEquals(1, fakeLocal.insertedBooks.size)
        val inserted = fakeLocal.insertedBooks.first()
        assertEquals("/works/OL200W", inserted.id)
        assertEquals("fiction", inserted.category)
        assertEquals("1984", inserted.title)
        assertEquals("George Orwell", inserted.author)
    }

    @Test
    fun testUpdateBookDescription() = runTest {
        val fakeLocal = FakeBooksLocalDataSource()
        val existingBook = Book(
            id = "/works/OL300W",
            category = "fiction",
            title = "Animal Farm",
            author = "George Orwell",
            coverId = 1,
            rating = 4.2,
            price = 12.5,
            description = null
        )
        fakeLocal.localBooks.add(existingBook)
        val detailsJson = """
            {
              "key": "/works/OL300W",
              "title": "Animal Farm",
              "description": "A political satire."
            }
        """.trimIndent()
        val repository = BooksRepositoryImpl(
            createApiService(detailsJson),
            fakeLocal,
            FakeNetworkManager()
        )

        assertEquals(RefreshResult.Success, repository.updateBook(existingBook.id))
        assertEquals("A political satire.", fakeLocal.getBookDetails(existingBook.id)?.description)
    }

    @Test
    fun testOfflineUpdateDoesNotChangeLocalBook() = runTest {
        val fakeLocal = FakeBooksLocalDataSource()
        val book = Book(
            id = "/works/OL301W",
            category = "fiction",
            title = "Book",
            author = "Author",
            coverId = 1,
            rating = 4.0,
            price = 10.0,
            description = "Original"
        )
        fakeLocal.localBooks.add(book)
        val repository = BooksRepositoryImpl(
            createApiService("{}"),
            fakeLocal,
            FakeNetworkManager(isAvailable = false)
        )

        assertEquals(RefreshResult.Offline, repository.updateBook(book.id))
        assertEquals("Original", fakeLocal.getBookDetails(book.id)?.description)
    }

    @Test
    fun testUpdateFailureReturnsErrorAndDoesNotChangeLocalBook() = runTest {
        val local = FakeBooksLocalDataSource()
        val book = Book(
            id = "/works/OL403W", category = "fiction", title = "Dune", author = "Frank Herbert",
            coverId = 1, rating = 4.0, price = 15.0, description = "Original"
        )
        local.localBooks += book
        val failingService = ApiService(HttpClient(MockEngine { throw IllegalStateException("Request failed") }))
        val repository = BooksRepositoryImpl(failingService, local, FakeNetworkManager())

        assertTrue(repository.updateBook(book.id) is RefreshResult.Error)
        assertEquals("Original", local.getBookDetails(book.id)?.description)
    }
}
