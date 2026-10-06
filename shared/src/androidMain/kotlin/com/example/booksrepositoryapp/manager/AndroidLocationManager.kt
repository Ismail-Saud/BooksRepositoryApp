package com.example.booksrepositoryapp.manager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.booksrepositoryapp.manager.locationManager.LocationCoordinates
import com.example.booksrepositoryapp.manager.locationManager.LocationHelper
import com.example.booksrepositoryapp.manager.locationManager.LocationLauncher
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class AndroidLocationManager(
    private val context: Context,
) : LocationHelper {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
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

    @Composable
    override fun rememberLocationLauncher(
        onLocationReceived: (addressId: String, latitude: Double, longitude: Double) -> Unit,
        onPermissionDenied: () -> Unit,
        onPermissionPermanentlyDenied: () -> Unit,
        onError: (String) -> Unit,
    ): LocationLauncher {
        var pendingAddressId by remember { mutableStateOf<String?>(null) }

        fun fetchLocationForAddress(addressId: String) {
            getCurrentLocation(
                onSuccess = { location ->
                    if (location != null) {
                        onLocationReceived(addressId, location.latitude, location.longitude)
                    } else {
                        onError("Unable to get location")
                    }
                },
                onFailure = {
                    onError("Failed to get location")
                },
            )
        }

        val permissionLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { isGranted ->
                if (isGranted) {
                    pendingAddressId?.let { id ->
                        fetchLocationForAddress(id)
                    }
                } else {
                    val activity = context as? Activity
                    val shouldShowRationale =
                        activity?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.ACCESS_FINE_LOCATION)
                        } ?: false
                    if (shouldShowRationale) {
                        onPermissionDenied()
                    } else {
                        onPermissionPermanentlyDenied()
                    }
                }
            }

        return remember(permissionLauncher) {
            object : LocationLauncher {
                override fun requestLocation(addressId: String) {
                    pendingAddressId = addressId
                    if (hasLocationPermission()) {
                        fetchLocationForAddress(addressId)
                    } else {
                        val activity = context as? Activity
                        if (activity != null && (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION))) {
                            onPermissionDenied()
                        }
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
    }

    override fun openAppSettings() {
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        context.startActivity(intent)
    }
}
