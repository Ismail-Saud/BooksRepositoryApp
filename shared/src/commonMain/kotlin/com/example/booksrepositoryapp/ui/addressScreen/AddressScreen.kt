package com.example.booksrepositoryapp.ui.addressScreen

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.data.source.local.uiModels.AddressUiModel
import com.example.booksrepositoryapp.helper.locationHelper.LocationHelper
import com.example.booksrepositoryapp.ui.conformationBottomSheet.ConfirmationBottomSheet
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AddressScreen(
    viewModel: AddressListViewModel,
    maxAddresses: Long,
    onBackClick: () -> Unit,
    locationHelper: LocationHelper = koinInject(),
) {
    val addresses by viewModel.addresses.collectAsState(initial = emptyList())
    val addressCount by viewModel.addressCount.collectAsState(initial = 0)

    var showPermissionDialog by remember { mutableStateOf(false) }
    var addressToDeleteId by remember { mutableStateOf<String?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val locationLauncher = locationHelper.rememberLocationLauncher(
        onLocationReceived = { addressId, latitude, longitude ->
            viewModel.onEvent(AddressListEvent.LocationReceived(addressId, latitude, longitude))
        },
        onPermissionDenied = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Location permission is required to get your address.")
            }
        },
        onPermissionPermanentlyDenied = {
            showPermissionDialog = true
        },
        onError = { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        },
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddressListEffect.NavigateBack -> onBackClick()
                is AddressListEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is AddressListEffect.RequestLocation -> {
                    locationLauncher.requestLocation(effect.addressId)
                }
                is AddressListEffect.ShowDeleteAllConfirmation -> showDeleteAllDialog = true
                is AddressListEffect.ShowDeleteAddressConfirmation -> addressToDeleteId = effect.addressId
                is AddressListEffect.OpenAppSettings -> {
                    showPermissionDialog = true
                }
            }
        }
    }

    if (showPermissionDialog) {
        ConfirmationBottomSheet(
            title = "Permission Required",
            message = "Location access was permanently denied. Please enable it in Settings to continue.",
            positiveButtonText = "Go to Settings",
            onConfirm = {
                locationHelper.openAppSettings()
                showPermissionDialog = false
            },
            onDismiss = { showPermissionDialog = false },
        )
    }

    if (showDeleteAllDialog) {
        ConfirmationBottomSheet(
            title = "Delete All",
            message = "Are you sure you want to delete all addresses?",
            positiveButtonText = "Delete All",
            onConfirm = {
                viewModel.onEvent(AddressListEvent.ConfirmDeleteAllAddresses)
                showDeleteAllDialog = false
            },
            onDismiss = { showDeleteAllDialog = false },
        )
    }

    if (addressToDeleteId != null) {
        ConfirmationBottomSheet(
            title = "Delete Address",
            message = "Do you want to delete this address?",
            positiveButtonText = "Delete",
            onConfirm = {
                addressToDeleteId?.let { id ->
                    viewModel.onEvent(AddressListEvent.ConfirmDeleteAddress(id))
                }
                addressToDeleteId = null
            },
            onDismiss = { addressToDeleteId = null },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                IconButton(
                    onClick = { viewModel.onEvent(AddressListEvent.BackClick) },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                    )
                }
                Text(
                    text = "Address List",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(
                    items = addresses,
                    key = { it.id },
                ) { uiModel ->
                    AddressItem(
                        uiModel = uiModel,
                        onLocationClick = {
                            viewModel.onEvent(AddressListEvent.GetLocation(uiModel))
                        },
                        onCheckClick = { editedAddress ->
                            viewModel.onEvent(AddressListEvent.SaveAddress(uiModel, editedAddress))
                        },
                        onDeleteClick = {
                            viewModel.onEvent(AddressListEvent.DeleteAddress(uiModel.id))
                        },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            ) {
                OutlinedButton(
                    onClick = { viewModel.onEvent(AddressListEvent.AddAddress(addressCount, maxAddresses.toInt())) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF555555)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF333333),
                    ),
                ) {
                    Text("Add Delivery Address")
                }
                Spacer(modifier = Modifier.height(18.dp))
                OutlinedButton(
                    onClick = { viewModel.onEvent(AddressListEvent.DeleteAllAddresses) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF555555)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF333333),
                    ),
                ) {
                    Text("Delete All Addresses")
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF151515)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
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
                    focusedTextColor = Color.White,
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onLocationClick, enabled = !isProcessing) {
                if (uiModel.isFetchingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.White,
                    )
                }
            }
            IconButton(onClick = { onCheckClick(addressText) }, enabled = !isProcessing) {
                if (uiModel.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        tint = Color.White,
                    )
                }
            }
            IconButton(onClick = onDeleteClick, enabled = !isProcessing) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                )
            }
        }
    }
}
