package com.example.booksrepositoryapp.manager.cameraManager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class AndroidCameraManager(
    private val context: Context,
) : CameraHelper {

    @Composable
    override fun rememberCameraLauncher(
        onImageCaptured: (String) -> Unit,
        onPermissionDenied: () -> Unit,
        onPermissionPermanentlyDenied: () -> Unit,
    ): CameraLauncher {
        var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

        val cameraLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
            ) { success ->
                if (success) {
                    cameraImageUri?.let { uri ->
                        onImageCaptured(uri.toString())
                    }
                }
            }

        fun createImageUri(): Uri {
            val fileName = "temp_profile_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }

        fun openCamera() {
            val uri = createImageUri()
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        }

        val cameraPermissionLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { granted ->
                if (granted) {
                    openCamera()
                } else {
                    val activity = context as? Activity
                    val shouldShowRationale =
                        activity?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
                        } ?: false
                    if (shouldShowRationale) {
                        onPermissionDenied()
                    } else {
                        onPermissionPermanentlyDenied()
                    }
                }
            }

        val galleryLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
            ) { uri ->
                uri?.let {
                    onImageCaptured(it.toString())
                }
            }

        return remember(cameraLauncher, cameraPermissionLauncher, galleryLauncher) {
            object : CameraLauncher {
                override fun launchCamera() {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA,
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            openCamera()
                        }

                        (context as? Activity)?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
                        } == true -> {
                            onPermissionDenied()
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }

                        else -> {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }

                override fun launchGallery() {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
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

    override fun getProfilePictureModel(fileName: String): Any? {
        if (fileName.isEmpty()) return null
        if (fileName.startsWith("content://") || fileName.startsWith("file://") || fileName.startsWith("http")) {
            return fileName
        }
        val file = File(context.filesDir, fileName)
        return if (file.exists()) file else null
    }
}
