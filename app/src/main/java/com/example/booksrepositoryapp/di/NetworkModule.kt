package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.data.source.remote.retrofit.ApiService
import com.example.booksrepositoryapp.data.source.remote.retrofit.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBooksApi(): ApiService {
        return RetrofitInstance.api
    }
}