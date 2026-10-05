package com.example.booksrepositoryapp.ui.accountDetails

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.booksrepositoryapp.helper.cameraHelper.CameraHelper
import com.example.booksrepositoryapp.ui.conformationBottomSheet.ConfirmationBottomSheet
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    viewModel: AccountDetailsViewModel,
    onNavigate: (AccountDetailsEffect) -> Unit,
    cameraHelper: CameraHelper = koinInject(),
) {
    val userState by viewModel.userState.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState(initial = null)

    var showPictureSheet by rememberSaveable { mutableStateOf(false) }
    var showRemovePictureSheet by rememberSaveable { mutableStateOf(false) }
    var showCameraSettingsSheet by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val cameraLauncher = cameraHelper.rememberCameraLauncher(
        onImageCaptured = { imageUri ->
            viewModel.onEvent(AccountDetailsEvent.ProfilePictureSelected(imageUri))
        },
        onPermissionDenied = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Camera permission is required to take a picture.")
            }
        },
        onPermissionPermanentlyDenied = {
            viewModel.onEvent(AccountDetailsEvent.CameraPermissionDeniedPermanent)
        },
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(AccountDetailsEvent.LoadUser)
        viewModel.effect.collect { effect ->
            when (effect) {
                AccountDetailsEffect.NavigateToLandingPage -> onNavigate(effect)
                is AccountDetailsEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(message = effect.message)
                }
                AccountDetailsEffect.OpenAppSettings -> {
                    showCameraSettingsSheet = true
                }
            }
        }
    }

    val errorState = userState as? AccountDetailsState.Error
    LaunchedEffect(errorState) {
        errorState?.let {
            snackbarHostState.showSnackbar(it.message)
        }
    }

    val user = (userState as? AccountDetailsState.Success)?.user
    val isLoading = userState is AccountDetailsState.Loading

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            AccountDetailsShimmer(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .clickable { showPictureSheet = true },
                        contentAlignment = Alignment.Center,
                    ) {
                        val profilePicture = user?.profilePicture
                        val imageModel = if (!profilePicture.isNullOrEmpty()) {
                            cameraHelper.getProfilePictureModel(profilePicture)
                        } else {
                            null
                        }

                        if (imageModel != null) {
                            AsyncImage(
                                model = imageModel,
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp),
                            )
                        }
                    }
                }

                item {
                    AccountInfoCard(
                        label = "Name:",
                        value = user?.username ?: "No name",
                        modifier = Modifier.padding(top = 32.dp),
                    )
                    AccountInfoCard(
                        label = "E-mail:",
                        value = user?.email ?: "No email",
                        modifier = Modifier.padding(top = 16.dp),
                    )

                    AccountInfoCard(
                        label = "Address:",
                        value = selectedAddress?.fullAddress ?: "No address selected",
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }

                item {
                    OutlinedButton(
                        onClick = {
                            viewModel.onEvent(AccountDetailsEvent.LogoutClicked)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, top = 24.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                        ),
                    ) {
                        Text(text = "Log out", fontSize = 14.sp)
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    if (showPictureSheet) {
        val hasProfilePicture = !user?.profilePicture.isNullOrEmpty()
        ModalBottomSheet(onDismissRequest = { showPictureSheet = false }) {
            ProfilePictureSheetContent(
                showRemoveOption = hasProfilePicture,
                onCameraClicked = {
                    showPictureSheet = false
                    cameraLauncher.launchCamera()
                },
                onGalleryClicked = {
                    showPictureSheet = false
                    cameraLauncher.launchGallery()
                },
                onRemoveClicked = {
                    showPictureSheet = false
                    showRemovePictureSheet = true
                },
            )
        }
    }

    if (showRemovePictureSheet) {
        ConfirmationBottomSheet(
            title = "Remove Profile Picture",
            message = "Are you sure to remove your profile picture?",
            positiveButtonText = "Remove",
            onConfirm = {
                viewModel.onEvent(AccountDetailsEvent.RemoveProfilePictureClicked)
                showRemovePictureSheet = false
            },
            onDismiss = {
                showRemovePictureSheet = false
            },
        )
    }

    if (showCameraSettingsSheet) {
        ConfirmationBottomSheet(
            title = "Camera Permission Required",
            message = "Camera permission is permanently denied. Please enable it in Settings to capture a profile picture.",
            positiveButtonText = "Open Settings",
            onConfirm = {
                showCameraSettingsSheet = false
                cameraHelper.openAppSettings()
            },
            onDismiss = {
                showCameraSettingsSheet = false
            },
        )
    }
}

@Composable
fun ProfilePictureSheetContent(
    showRemoveOption: Boolean,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        ListItem(
            headlineContent = { Text("Take a photo") },
            leadingContent = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
            modifier = Modifier.clickable { onCameraClicked() },
        )
        ListItem(
            headlineContent = { Text("Choose from gallery") },
            leadingContent = { Icon(Icons.Default.Photo, contentDescription = null) },
            modifier = Modifier.clickable { onGalleryClicked() },
        )
        if (showRemoveOption) {
            ListItem(
                headlineContent = { Text("Remove photo") },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null) },
                modifier = Modifier.clickable { onRemoveClicked() },
            )
        }
    }
}

@Composable
fun AccountInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 55.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.CenterVertically),
                color = Color.Black,
            )
            Text(
                text = value,
                modifier = Modifier.weight(1f),
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun AccountDetailsShimmer(modifier: Modifier = Modifier) {
    val shimmerBrush = rememberShimmerBrush()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(width = 90.dp, height = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush),
        )

        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .size(90.dp)
                .clip(CircleShape)
                .background(shimmerBrush),
        )

        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .padding(top = if (index == 0) 32.dp else 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 55.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(shimmerBrush),
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 24.dp)
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(shimmerBrush),
        )
    }
}

@Composable
private fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim, translateAnim),
    )
}
