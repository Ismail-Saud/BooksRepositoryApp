package com.example.booksrepositoryapp.helper.cameraHelper

import androidx.compose.runtime.Composable

interface CameraLauncher {
    fun launchCamera()
    fun launchGallery()
}

interface CameraHelper {
    @Composable
    fun rememberCameraLauncher(
        onImageCaptured: (String) -> Unit,
        onPermissionDenied: () -> Unit = {},
        onPermissionPermanentlyDenied: () -> Unit = {},
    ): CameraLauncher

    fun openAppSettings()

    fun getProfilePictureModel(fileName: String): Any?
}
