package com.example.booksrepositoryapp.manager.locationManager

import androidx.compose.runtime.Composable

interface LocationLauncher {
    fun requestLocation(addressId: String)
}

interface LocationHelper {
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String?
    suspend fun getLocationFromAddress(fullAddress: String): LocationCoordinates?

    @Composable
    fun rememberLocationLauncher(
        onLocationReceived: (addressId: String, latitude: Double, longitude: Double) -> Unit,
        onPermissionDenied: () -> Unit = {},
        onPermissionPermanentlyDenied: () -> Unit = {},
        onError: (String) -> Unit = {},
    ): LocationLauncher

    fun openAppSettings()
}
