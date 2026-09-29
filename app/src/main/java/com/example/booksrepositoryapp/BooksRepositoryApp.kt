package com.example.booksrepositoryapp

import android.app.Application
import com.example.booksrepositoryapp.di.androidNetworkModule
import com.example.booksrepositoryapp.di.androidRepositoryModule
import com.example.booksrepositoryapp.di.commonViewModelModule
import com.example.booksrepositoryapp.di.firebaseModule
import com.example.booksrepositoryapp.di.networkModule
import com.example.booksrepositoryapp.di.repositoryModule
import com.example.booksrepositoryapp.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BooksRepositoryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BooksRepositoryApp)
            modules(
                firebaseModule,
                androidNetworkModule,
                androidRepositoryModule,
                networkModule,
                repositoryModule,
                roomModule,
                commonViewModelModule
            )
        }
    }
}
