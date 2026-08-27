package com.example.booksrepositoryapp.ui.account_details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import android.Manifest

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheetCompose


import java.io.File

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    viewModel: AccountDetailsViewModel,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val selectedAddress by viewModel.selectedAddress.collectAsStateWithLifecycle(initialValue = null)
    val imageUri by viewModel.imageUri.observeAsState()

    var showPictureSheet by remember { mutableStateOf(false) }
    var showRemovePictureSheet by remember { mutableStateOf(false) }
    var showGoToSettingsDialog by remember { mutableStateOf(false) }
    var showPermissionDeniedToast by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri?.let { uri -> viewModel.saveUserProfilePicture(uri) }
        }
    }

    fun createImageUri(): Uri {
        clearOldProfilePictures(context)
        val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun openCamera() {
        val uri = createImageUri()
        viewModel.setImageUri(uri)
        cameraLauncher.launch(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            openCamera()
        } else {
            val activity = context as? Activity
            val shouldShowRationale = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
            } ?: false
            if (shouldShowRationale) {
                showPermissionDeniedToast = true
            } else {
                showGoToSettingsDialog = true
            }
        }
    }

    fun onCameraClicked() {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()

            (context as? Activity)?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
            } == true -> {
                Toast.makeText(context, "Camera permission is required to take a picture.", Toast.LENGTH_SHORT).show()
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val savedUri = copyImageToInternalStorage(context, it)
            viewModel.saveUserProfilePicture(savedUri)
        }
    }

    fun onGalleryClicked() {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    LaunchedEffect(showPermissionDeniedToast) {
        if (showPermissionDeniedToast) {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            showPermissionDeniedToast = false
        }
    }

    val errorState = userState as? AccountDetailsState.Error
    LaunchedEffect(errorState) {
        errorState?.let {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
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
                        contentAlignment = Alignment.Center
                    ) {
                        if (!user?.profilePicture.isNullOrEmpty()) {
                            GlideImage(
                                model = user?.profilePicture,
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }

                item {
                    AccountInfoCard(
                        label = "Name:",
                        value = user?.username ?: "Name not found",
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    AccountInfoCard(
                        label = "E-mail:",
                        value = user?.email ?: "Email not found",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    AccountInfoCard(
                        label = "Address:",
                        value = selectedAddress?.fullAddress ?: "No address selected",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                item {
                    OutlinedButton(
                        onClick = {
                            viewModel.logout()
                            onLogoutClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, top = 24.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Log out", fontSize = 14.sp)
                    }
                }
            }
        }
    }

    if (showPictureSheet) {
        val hasProfilePicture = !user?.profilePicture.isNullOrEmpty()
        ModalBottomSheet(onDismissRequest = { showPictureSheet = false }) {
            ProfilePictureSheetContent(
                showRemoveOption = hasProfilePicture,
                onCameraClicked = {
                    showPictureSheet = false
                    onCameraClicked()
                },
                onGalleryClicked = {
                    showPictureSheet = false
                    onGalleryClicked()
                },
                onRemoveClicked = {
                    showPictureSheet = false
                    showRemovePictureSheet = true
                }
            )
        }
    }

    if (showRemovePictureSheet) {
        ConfirmationBottomSheetCompose(
            title = "Remove Profile Picture",
            message = "Are you sure to remove your profile picture?",
            positiveButtonText = "Remove",

            onConfirm = {
                viewModel.removeUserProfilePicture()
                clearOldProfilePictures(context)
                showRemovePictureSheet = false
            },

            onDismiss = {
                showRemovePictureSheet = false
            }
        )
    }

    if (showGoToSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showGoToSettingsDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Camera access was permanently denied. Please enable it in Settings to continue.") },
            confirmButton = {
                TextButton(onClick = {
                    showGoToSettingsDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Go to Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoToSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfilePictureSheetContent(
    showRemoveOption: Boolean,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        ListItem(
            headlineContent = { Text("Take a photo") },
            leadingContent = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
            modifier = Modifier.clickable { onCameraClicked() }
        )
        ListItem(
            headlineContent = { Text("Choose from gallery") },
            leadingContent = { Icon(Icons.Default.Photo, contentDescription = null) },
            modifier = Modifier.clickable { onGalleryClicked() }
        )
        if (showRemoveOption) {
            ListItem(
                headlineContent = { Text("Remove photo") },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null) },
                modifier = Modifier.clickable { onRemoveClicked() }
            )
        }
    }
}

@Composable
fun AccountInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 55.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.CenterVertically),
                color = Color.Black
            )
            Text(
                text = value,
                modifier = Modifier.weight(1f),
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(width = 90.dp, height = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush)
        )

        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .size(90.dp)
                .clip(CircleShape)
                .background(shimmerBrush)
        )

        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .padding(top = if (index == 0) 32.dp else 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 55.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(shimmerBrush)
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 24.dp)
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(shimmerBrush)
        )
    }
}

@Composable
private fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim, translateAnim)
    )
}

private fun clearOldProfilePictures(context: Context) {
    context.filesDir.listFiles()?.forEach { file ->
        if (file.name.startsWith("profile_picture_") && file.name.endsWith(".jpg")) {
            file.delete()
        }
    }
}

private fun copyImageToInternalStorage(context: Context, uri: Uri): Uri {
    clearOldProfilePictures(context)
    val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)
    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

@Preview(showBackground = true)
@Composable
fun AddToCartPreview() {
    BooksRepositoryAppTheme {
        AccountDetailsScreen(
            viewModel = viewModel(),
            onLogoutClick = {}
        )
    }
}