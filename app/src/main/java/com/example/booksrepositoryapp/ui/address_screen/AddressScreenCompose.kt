package com.example.booksrepositoryapp.ui.address_screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.helper.LocationHelper
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheetCompose
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme
import kotlinx.coroutines.launch

@Composable
fun AddressScreenCompose(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel: AddressListViewModel = viewModel()
    val addresses by viewModel.addresses.collectAsState(
        initial = emptyList()
    )
    val locationHelper = remember {
        LocationHelper(context)
    }
    var addressBeingLocated by remember {
        mutableStateOf<AddressModel?>(null)
    }
    var showPermissionDialog by remember {
        mutableStateOf(false)
    }
    var addressToDelete by remember {
        mutableStateOf<AddressModel?>(null)
    }
    fun getAddressFromLocation(
        latitude: Double,
        longitude: Double
    ) {
        val target = addressBeingLocated ?: return
        scope.launch {
            val locationAddress = locationHelper.getAddressFromLocation(latitude, longitude)
            if (locationAddress == null) {
                viewModel.updateAddress(
                    target.copy(
                        isFetchingLocation = false
                    )
                )
                Toast.makeText(
                    context,
                    "Unable to get address",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val updatedAddress = target.copy(
                house = locationAddress.featureName ?: "",
                street = locationAddress.thoroughfare,
                area = locationAddress.subLocality ?: "",
                city = locationAddress.locality ?: locationAddress.subAdminArea ?: "",
                postalCode = locationAddress.postalCode,
                country = locationAddress.countryName ?: "",
                fullAddress = locationAddress.getAddressLine(0) ?: "",
                latitude = latitude,
                longitude = longitude,
                isFetchingLocation = false,
                isSelected = true
            )
            viewModel.updateAddress(updatedAddress)
            addressBeingLocated = null
            Toast.makeText(
                context,
                "Address updated",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getCurrentLocation() {
        locationHelper.getCurrentLocation(
            onSuccess = { location ->
                if (location != null) {
                    getAddressFromLocation(location.latitude, location.longitude)
                } else {
                    addressBeingLocated?.let { address ->
                        viewModel.updateAddress(
                            address.copy(
                                isFetchingLocation = false
                            )
                        )
                    }
                    Toast.makeText(
                        context,
                        "Unable to get current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },

            onFailure = {
                addressBeingLocated?.let { address ->
                    viewModel.updateAddress(
                        address.copy(
                            isFetchingLocation = false
                        )
                    )
                }
                Toast.makeText(
                    context,
                    "Failed to get location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    fun getLocationFromAddress(address: AddressModel, fullAddress: String) {
        viewModel.updateAddress(
            address.copy(
                isSaving = true
            )
        )
        scope.launch {
            val location = locationHelper.getLocationFromAddress(fullAddress)
            if (location == null) {
                viewModel.updateAddress(
                    address.copy(
                        isSaving = false
                    )
                )
                Toast.makeText(
                    context,
                    "Address not found",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val updatedAddress = address.copy(
                house = location.subThoroughfare ?: "",
                street = location.thoroughfare,
                area = location.subLocality ?: location.featureName ?: "",
                city = location.locality ?: location.subAdminArea ?: "",
                postalCode = location.postalCode,
                country = location.countryName ?: "",
                fullAddress = fullAddress,
                latitude = location.latitude,
                longitude = location.longitude,
                isSaving = false
            )
            viewModel.updateAddress(updatedAddress)
            viewModel.updateSelectedAddress(updatedAddress.id)
            Toast.makeText(
                context,
                "Address updated successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(
                    context,
                    "Location permission granted",
                    Toast.LENGTH_SHORT
                ).show()
                getCurrentLocation()
            } else {
                showPermissionDialog = true
            }
        }

    fun checkLocationPermission(address: AddressModel) {
        addressBeingLocated = address
        when {
            locationHelper.hasLocationPermission() -> {
                getCurrentLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(
                    context,
                    "Location permission is required to get your current address.",
                    Toast.LENGTH_LONG
                ).show()
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            else -> {
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    if (showPermissionDialog) {
        ConfirmationBottomSheetCompose(
            title = "Permission Required",
            message = "Location access was permanently denied. Please enable it in Settings to continue.",
            positiveButtonText = "Go to Settings",
            onConfirm = {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                ).apply {
                    data = Uri.fromParts(
                        "package",
                        context.packageName,
                        null
                    )
                }
                context.startActivity(intent)
            },
            onDismiss = {
                showPermissionDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Address List",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = addresses,
                key = { it.id }
            ) { address ->
                AddressItem(
                    address = address,
                    onLocationClick = {
                        viewModel.updateAddress(
                            address.copy(
                                isFetchingLocation = true
                            )
                        )
                        checkLocationPermission(address)
                    },
                    onCheckClick = { editedAddress ->
                        getLocationFromAddress(address = address, fullAddress = editedAddress)
                    },
                    onDeleteClick = {
                        addressToDelete = address
                    }
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                )
        ) {
            OutlinedButton(
                onClick = {
                    onAddClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    1.dp,
                    Color(0xFF555555)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF333333)
                )
            ) {
                Text("Add Delivery Address")
            }
            Spacer(
                modifier = Modifier.height(18.dp)
            )
            OutlinedButton(
                onClick = {
                    onDeleteClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    1.dp,
                    Color(0xFF555555)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF333333)
                )
            ) {
                Text("Delete All Addresses")
            }
        }
    }
    if (addressToDelete != null) {
        ConfirmationBottomSheetCompose(
            title = "Delete Address",
            message = "Do you want to delete this address?",
            positiveButtonText = "Delete",
            onConfirm = {
                addressToDelete?.let { address ->
                    viewModel.deleteAddress(address)
                }
                addressToDelete = null
            },
            onDismiss = {
                addressToDelete = null
            }
        )
    }
}

@Composable
fun AddressItem(
    address: AddressModel,
    onLocationClick: () -> Unit,
    onCheckClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
)   {
    var addressText by rememberSaveable(address.id) {
        mutableStateOf(address.fullAddress)
    }
    LaunchedEffect(address.fullAddress) {
        addressText = address.fullAddress
    }
    val isProcessing = address.isFetchingLocation || address.isSaving
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF151515)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = addressText,
                onValueChange = { newText ->
                    addressText = newText
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text("Delivery Address")
                },
                maxLines = 3,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onLocationClick,
                enabled = !isProcessing
            ) {
                if (address.isFetchingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.White
                    )
                }
            }
            IconButton(
                onClick = {
                    onCheckClick(addressText)
                },
                enabled = !isProcessing
            ) {
                if (address.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        tint = Color.White
                    )
                }
            }
            IconButton(
                onClick = onDeleteClick,
                enabled = !isProcessing
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    BooksRepositoryAppTheme {
        AddressScreenCompose (
            onBackClick = {},
            onAddClick = {},
            onDeleteClick = {}
        )
    }
}