package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.manager.AndroidLocationManager
import com.example.booksrepositoryapp.manager.cameraManager.AndroidCameraManager
import com.example.booksrepositoryapp.manager.cameraManager.CameraHelper
import com.example.booksrepositoryapp.manager.locationManager.LocationHelper
import com.example.booksrepositoryapp.manager.networkManager.AndroidNetworkManager
import com.example.booksrepositoryapp.manager.networkManager.NetworkManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidNetworkModule = module {
    single<NetworkManager> {
        AndroidNetworkManager(androidContext())
    }
    single<LocationHelper> {
        AndroidLocationManager(androidContext())
    }
    single<CameraHelper> {
        AndroidCameraManager(androidContext())
    }
}
