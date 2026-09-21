package com.example.booksrepositoryapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.booksrepositoryapp.di.firebaseModule
import com.example.booksrepositoryapp.di.networkModule
import com.example.booksrepositoryapp.di.repositoryModules
import com.example.booksrepositoryapp.di.roomModule
import com.example.booksrepositoryapp.di.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BooksRepositoryApp : Application() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BooksRepositoryApp)
            modules(
                firebaseModule,
                networkModule,
                repositoryModules,
                roomModule,
                viewmodelModule
            )
        }
    }
}