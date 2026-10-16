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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import booksrepositoryapp.shared.generated.resources.Res
import booksrepositoryapp.shared.generated.resources.account
import booksrepositoryapp.shared.generated.resources.address
import booksrepositoryapp.shared.generated.resources.camera_permission_message
import booksrepositoryapp.shared.generated.resources.camera_permission_required
import booksrepositoryapp.shared.generated.resources.camera_permission_title
import booksrepositoryapp.shared.generated.resources.choose_from_gallery
import booksrepositoryapp.shared.generated.resources.email
import booksrepositoryapp.shared.generated.resources.language
import booksrepositoryapp.shared.generated.resources.logout
import booksrepositoryapp.shared.generated.resources.name
import booksrepositoryapp.shared.generated.resources.no_address_selected
import booksrepositoryapp.shared.generated.resources.no_email
import booksrepositoryapp.shared.generated.resources.no_name
import booksrepositoryapp.shared.generated.resources.open_settings
import booksrepositoryapp.shared.generated.resources.profile_photo
import booksrepositoryapp.shared.generated.resources.remove
import booksrepositoryapp.shared.generated.resources.remove_photo
import booksrepositoryapp.shared.generated.resources.remove_profile_picture_message
import booksrepositoryapp.shared.generated.resources.remove_profile_picture_title
import booksrepositoryapp.shared.generated.resources.take_a_photo
import coil3.compose.AsyncImage
import com.example.booksrepositoryapp.localization.AppLanguage
import com.example.booksrepositoryapp.localization.customAppLocale
import com.example.booksrepositoryapp.manager.cameraManager.CameraHelper
import com.example.booksrepositoryapp.ui.conformationBottomSheet.ConfirmationBottomSheet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    viewModel: AccountDetailsViewModel,
    onNavigate: (AccountDetailsEffect) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    cameraHelper: CameraHelper = koinInject(),
) {
    val userState by viewModel.userState.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState(initial = null)

    var showPictureSheet by rememberSaveable { mutableStateOf(false) }
    var showRemovePictureSheet by rememberSaveable { mutableStateOf(false) }
    var showCameraSettingsSheet by rememberSaveable { mutableStateOf(false) }
    var selectedLanguage = when (customAppLocale) {
        "ur" -> "اردو"
        "en" -> "English"
        else -> "English"
    }
    var languageDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val cameraPermissionRequiredMessage = stringResource(Res.string.camera_permission_required)

    val cameraLauncher = cameraHelper.rememberCameraLauncher(
        onImageCaptured = { imageUri ->
            viewModel.onEvent(AccountDetailsEvent.ProfilePictureSelected(imageUri))
        },
        onPermissionDenied = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(cameraPermissionRequiredMessage)
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
                        text = stringResource(Res.string.account),
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
                                contentDescription = stringResource(Res.string.profile_photo),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = stringResource(Res.string.profile_photo),
                                tint = Color.White,
                                modifier = Modifier.size(50.dp),
                            )
                        }
                    }
                }

                item {
                    AccountInfoCard(
                        label = "${stringResource(Res.string.name)}:",
                        value = user?.username ?: stringResource(Res.string.no_name),
                        modifier = Modifier.padding(top = 32.dp),
                    )
                    AccountInfoCard(
                        label = "${stringResource(Res.string.email)}:",
                        value = user?.email ?: stringResource(Res.string.no_email),
                        modifier = Modifier.padding(top = 16.dp),
                    )

                    AccountInfoCard(
                        label = "${stringResource(Res.string.address)}:",
                        value = selectedAddress?.fullAddress ?: stringResource(Res.string.no_address_selected),
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }


                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = stringResource(Res.string.language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = languageDropdownExpanded,
                            onExpandedChange = {
                                languageDropdownExpanded = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedTextField(
                                value = selectedLanguage,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = languageDropdownExpanded,
                                    )
                                },
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                ),
                            )

                            val languages = AppLanguage.entries

                            ExposedDropdownMenu(
                                expanded = languageDropdownExpanded,
                                onDismissRequest = {
                                    languageDropdownExpanded = false
                                }
                            ) {
                                languages.forEach { language ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = language.displayName)
                                        },
                                        onClick = {
                                            selectedLanguage = language.displayName
                                            languageDropdownExpanded = false

                                            onLanguageSelected(language)
                                        }
                                    )
                                }
                            }
                        }
                    }
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
                        Text(text = stringResource(Res.string.logout), fontSize = 14.sp)
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
            title = stringResource(Res.string.remove_profile_picture_title),
            message = stringResource(Res.string.remove_profile_picture_message),
            positiveButtonText = stringResource(Res.string.remove),
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
            title = stringResource(Res.string.camera_permission_title),
            message = stringResource(Res.string.camera_permission_message),
            positiveButtonText = stringResource(Res.string.open_settings),
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
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 24.dp),
    ) {
        Text(
            text = stringResource(Res.string.profile_photo),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111111),
        )
        Spacer(modifier = Modifier.height(16.dp))
        PhotoOptionRow(
            icon = Icons.Default.CameraAlt,
            label = stringResource(Res.string.take_a_photo),
            onClick = onCameraClicked,
        )
        PhotoOptionRow(
            icon = Icons.Default.Photo,
            label = stringResource(Res.string.choose_from_gallery),
            onClick = onGalleryClicked,
        )
        if (showRemoveOption) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFEEEEEE),
            )
            PhotoOptionRow(
                icon = Icons.Default.Delete,
                label = stringResource(Res.string.remove_photo),
                onClick = onRemoveClicked,
                isDestructive = true,
            )
        }
    }
}

@Composable
private fun PhotoOptionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
) {
    val contentColor = if (isDestructive) Color(0xFFD32F2F) else Color(0xFF111111)
    val iconBackground = if (isDestructive) Color(0xFFFDECEA) else Color(0xFFF2F2F2)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
                .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor,
            modifier = Modifier.weight(1f),
        )
        if (!isDestructive) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFBDBDBD),
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
