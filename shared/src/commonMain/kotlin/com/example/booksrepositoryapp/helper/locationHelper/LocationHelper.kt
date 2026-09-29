package com.example.booksrepositoryapp.helper.locationHelper

interface LocationHelper {
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String?
    suspend fun getLocationFromAddress(fullAddress: String): LocationCoordinates?
}