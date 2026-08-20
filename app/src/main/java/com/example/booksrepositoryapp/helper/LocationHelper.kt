package com.example.booksrepositoryapp.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationHelper(
    private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onSuccess: (Location?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (!hasLocationPermission()) {
            return
        }
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            onSuccess(location)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
    suspend fun getAddressFromLocation(
        latitude: Double,
        longitude: Double
    ): Address? {
        return withContext(Dispatchers.IO) {
            try {
                Geocoder(
                    context,
                    Locale.getDefault()
                ).getFromLocation(
                    latitude,
                    longitude,
                    1
                )?.firstOrNull()

            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getLocationFromAddress(
        fullAddress: String
    ): Address? {

        return withContext(Dispatchers.IO) {
            try {
                Geocoder(
                    context,
                    Locale.getDefault()
                ).getFromLocationName(
                    fullAddress,
                    1
                )?.firstOrNull()

            } catch (e: Exception) {
                null
            }
        }
    }
}