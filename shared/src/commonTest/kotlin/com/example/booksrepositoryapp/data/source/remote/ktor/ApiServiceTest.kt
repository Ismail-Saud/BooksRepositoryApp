package com.example.booksrepositoryapp.data.source.remote.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ApiServiceTest {

    private fun createMockClient(jsonResponse: String, status: HttpStatusCode = HttpStatusCode.OK): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }
    }

    @Test
    fun testValidSubjectJsonParsing() = runTest {
        val json = """
            {
              "works": [
                {
                  "key": "/works/OL100W",
                  "title": "The Hobbit",
                  "cover_id": 12345,
                  "authors": [
                    { "key": "/authors/OL1A", "name": "J.R.R. Tolkien" }
                  ]
                }
              ]
            }
        """.trimIndent()

        val apiService = ApiService(createMockClient(json))
        val response = apiService.getBooksByCategory("fantasy")

        assertEquals(1, response.works.size)
        val work = response.works.first()
        assertEquals("/works/OL100W", work.key)
        assertEquals("The Hobbit", work.title)
        assertEquals(12345, work.cover_id)
        assertEquals(1, work.authors.size)
        assertEquals("J.R.R. Tolkien", work.authors.first().name)
    }

    @Test
    fun testBookDetailsResponseWithObjectDescription() = runTest {
        val json = """
            {
              "key": "/works/OL100W",
              "title": "The Hobbit",
              "description": {
                "type": "/type/text",
                "value": "An adventurous story of Bilbo Baggins."
              }
            }
        """.trimIndent()

        val apiService = ApiService(createMockClient(json))
        val response = apiService.getBookDetails("works/OL100W")

        assertNotNull(response)
        assertEquals("/works/OL100W", response.key)
        assertEquals("The Hobbit", response.title)
        assertEquals("An adventurous story of Bilbo Baggins.", response.description)
    }

    @Test
    fun testBookDetailsResponseWithPlainStringDescription() = runTest {
        val json = """
            {
              "key": "/works/OL100W",
              "title": "The Hobbit",
              "description": "Plain string description of Bilbo Baggins."
            }
        """.trimIndent()

        val apiService = ApiService(createMockClient(json))
        val response = apiService.getBookDetails("works/OL100W")

        assertNotNull(response)
        assertEquals("Plain string description of Bilbo Baggins.", response.description)
    }
}
