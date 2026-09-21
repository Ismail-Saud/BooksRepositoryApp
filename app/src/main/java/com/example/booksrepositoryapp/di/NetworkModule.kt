package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.BuildConfig
import com.example.booksrepositoryapp.data.source.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                level = if (BuildConfig.DEBUG) {
                    LogLevel.BODY
                } else {
                    LogLevel.NONE
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 12_000L
                connectTimeoutMillis = 12_000L
                socketTimeoutMillis = 12_000L
            }

            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnServerErrors(maxRetries = 3)
                retryOnException(maxRetries = 3)
            }

            defaultRequest {
                url("https://openlibrary.org/")
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(
        client: HttpClient
    ): ApiService {
        return ApiService(client)
    }
}