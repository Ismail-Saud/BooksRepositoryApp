package com.example.booksrepositoryapp.data.source.remote.ktor.httpClient

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException

actual fun createHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        expectSuccess = true
        defaultRequest {
            url("https://openlibrary.org/")
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(HttpRequestRetry) {
            maxRetries = 3
            retryIf { _, response ->
                response.status.value in 500..599
            }
            retryOnExceptionIf { _, cause ->
                cause is HttpRequestTimeoutException ||
                        cause is ConnectTimeoutException ||
                        cause is SocketTimeoutException ||
                        cause is ConnectException
            }
            exponentialDelay()
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 120_000
            requestTimeoutMillis = 120_000
            socketTimeoutMillis = 120_000
        }
    }
}