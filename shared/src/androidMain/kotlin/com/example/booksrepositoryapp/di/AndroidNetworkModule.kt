package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.helper.networkHelper.AndroidNetworkHelper
import com.example.booksrepositoryapp.helper.networkHelper.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidNetworkModule = module {
    single<NetworkHelper> {
        AndroidNetworkHelper(androidContext())
    }
}
