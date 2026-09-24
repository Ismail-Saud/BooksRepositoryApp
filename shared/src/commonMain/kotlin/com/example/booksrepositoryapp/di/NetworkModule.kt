package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.remote.ktor.ApiService
import com.example.booksrepositoryapp.data.source.remote.ktor.httpClient.createHttpClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<ApiService> {
        ApiService(get())
    }

    single<HttpClient> {
        createHttpClient()
    }
}