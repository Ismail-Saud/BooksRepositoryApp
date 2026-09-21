package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.remote.retrofit.ApiService
import com.example.booksrepositoryapp.data.source.remote.retrofit.RetrofitInstance
import com.example.booksrepositoryapp.helper.networkHelper.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single<ApiService> {
        RetrofitInstance.api
    }

    single<NetworkHelper> {
        NetworkHelper(androidContext())
    }
}