package com.example.booksrepositoryapp.ui.addressScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.helper.LocationHelper
import com.example.booksrepositoryapp.ui.conformationBottomSheet.ConfirmationBottomSheetCompose
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
        mutableStateOf<AddressUiModel?>(null)
    }
    var showPermissionDialog by remember {
        mutableStateOf(false)
    }
    var addressToDelete by remember {
        mutableStateOf<AddressUiModel?>(null)
    }

    fun getAddressFromLocation(
        latitude: Double,
        longitude: Double
    ) {
        val target = addressBeingLocated ?: return
        scope.launch {
            val locationAddress = locationHelper.getAddressFromLocation(latitude, longitude)
            if (locationAddress == null) {
                viewModel.setFetchingLocation(target.id, false)
                Toast.makeText(
                    context,
                    "Unable to get address",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val updatedAddress = target.address.copy(
                house = locationAddress.featureName ?: "",
                street = locationAddress.thoroughfare ?: "",
                area = locationAddress.subLocality ?: "",
                city = locationAddress.locality ?: locationAddress.subAdminArea ?: "",
                postalCode = locationAddress.postalCode ?: "N/A",
                country = locationAddress.countryName ?: "",
                fullAddress = locationAddress.getAddressLine(0) ?: "",
                latitude = latitude,
                longitude = longitude,
                isSelected = true
            )
            viewModel.updateAddress(updatedAddress)
            viewModel.setFetchingLocation(target.id, false)
            addressBeingLocated = null
            Toast.makeText(
                context,
                "Address updated",
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
            locationHelper.getCurrentLocation(
                onSuccess = { location ->
                    if (location != null) {
                        getAddressFromLocation(location.latitude, location.longitude)
                    } else {
                        addressBeingLocated?.let { ui ->
                            viewModel.setFetchingLocation(ui.id, false)
                        }
                        Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = {
                    addressBeingLocated?.let { ui ->
                        viewModel.setFetchingLocation(ui.id, false)
                    }
                    Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            showPermissionDialog = true
        }
    }

    fun checkLocationPermission(addressUi: AddressUiModel) {
        addressBeingLocated = addressUi
        when {
            locationHelper.hasLocationPermission() -> {
                locationHelper.getCurrentLocation(
                    onSuccess = { location ->
                        if (location != null) {
                            getAddressFromLocation(location.latitude, location.longitude)
                        } else {
                            viewModel.setFetchingLocation(addressUi.id, false)
                            Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailure = {
                        viewModel.setFetchingLocation(addressUi.id, false)
                        Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                    }
                )
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
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    fun getLocationFromAddress(addressUi: AddressUiModel, fullAddress: String) {
        viewModel.setSaving(addressUi.id, true)
        scope.launch {
            val location = locationHelper.getLocationFromAddress(fullAddress)
            if (location == null) {
                viewModel.setSaving(addressUi.id, false)
                Toast.makeText(
                    context,
                    "Address not found",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val updatedAddress = addressUi.address.copy(
                house = location.subThoroughfare ?: "",
                street = location.thoroughfare ?: "",
                area = location.subLocality ?: location.featureName ?: "",
                city = location.locality ?: location.subAdminArea ?: "",
                postalCode = location.postalCode ?: "N/A",
                country = location.countryName ?: "",
                fullAddress = fullAddress,
                latitude = location.latitude,
                longitude = location.longitude
            )
            viewModel.updateAddress(updatedAddress)
            viewModel.updateSelectedAddress(updatedAddress.id)
            viewModel.setSaving(addressUi.id, false)
            Toast.makeText(
                context,
                "Address updated successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (showPermissionDialog) {
        ConfirmationBottomSheetCompose(
            title = "Permission Required",
            message = "Location access was permanently denied. Please enable it in Settings to continue.",
            positiveButtonText = "Go to Settings",
            onConfirm = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            onDismiss = {
                showPermissionDialog = false
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
            ) { uiModel ->
                AddressItem(
                    uiModel = uiModel,
                    onLocationClick = {
                        viewModel.setFetchingLocation(uiModel.id, true)
                        checkLocationPermission(uiModel)
                    },
                    onCheckClick = { editedAddress ->
                        getLocationFromAddress(addressUi = uiModel, fullAddress = editedAddress)
                    },
                    onDeleteClick = {
                        addressToDelete = uiModel
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            OutlinedButton(
                onClick = { onAddClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0xFF555555)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF333333)
                )
            ) {
                Text("Add Delivery Address")
            }
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedButton(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0xFF555555)),
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
                addressToDelete?.let { ui ->
                    viewModel.deleteAddress(ui.id)
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
    uiModel: AddressUiModel,
    onLocationClick: () -> Unit,
    onCheckClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
) {
    var addressText by rememberSaveable(uiModel.id) {
        mutableStateOf(uiModel.address.fullAddress)
    }
    LaunchedEffect(uiModel.address.fullAddress) {
        addressText = uiModel.address.fullAddress
    }
    val isProcessing = uiModel.isFetchingLocation || uiModel.isSaving
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF151515))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = addressText,
                onValueChange = { addressText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Delivery Address") },
                maxLines = 3,
                textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 16.sp),
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
            IconButton(onClick = onLocationClick, enabled = !isProcessing) {
                if (uiModel.isFetchingLocation) {
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
            IconButton(onClick = { onCheckClick(addressText) }, enabled = !isProcessing) {
                if (uiModel.isSaving) {
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
            IconButton(onClick = onDeleteClick, enabled = !isProcessing) {
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
        AddressScreenCompose(
            onBackClick = {},
            onAddClick = {},
            onDeleteClick = {}
        )
    }
}
