package com.example.booksrepositoryapp.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.booksrepositoryapp.helper.locationHelper.LocationCoordinates
import com.example.booksrepositoryapp.helper.locationHelper.LocationHelper
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class AndroidLocationHelper(
    private val context: Context
) : LocationHelper {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onSuccess: (Location?) -> Unit, onFailure: (Exception) -> Unit) {
        if (!hasLocationPermission()) {
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { location ->
            onSuccess(location)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
    override suspend fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0)
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getLocationFromAddress(fullAddress: String): LocationCoordinates? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                geocoder
                    .getFromLocationName(fullAddress, 1)
                    ?.firstOrNull()
                    ?.let { address ->
                        LocationCoordinates(latitude = address.latitude, longitude = address.longitude)
                    }
            } catch (e: Exception) {
                null
            }
        }
    }
}