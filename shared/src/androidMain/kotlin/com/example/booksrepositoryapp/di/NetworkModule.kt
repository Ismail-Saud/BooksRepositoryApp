package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.remote.ktor.httpClient.createHttpClient
import com.example.booksrepositoryapp.helper.networkHelper.AndroidNetworkHelper
import com.example.booksrepositoryapp.helper.networkHelper.NetworkHelper
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidNetworkModule = module {
    single<HttpClient> {
        createHttpClient()
    }
    single<NetworkHelper> {
        AndroidNetworkHelper(
            androidContext()
        )
    }
}