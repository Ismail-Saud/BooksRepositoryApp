package com.example.booksrepositoryapp.ui.account_details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentAccountDetailsBinding
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.example.booksrepositoryapp.ui.landingpage.LandingPageFragment
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.booksrepositoryapp.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountDetailsFragment : Fragment(), OnPictureOptionSelected {

    private var _binding: FragmentAccountDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
        viewModel.getUser()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { state ->
                    when (state) {
                        is AccountDetailsState.Loading -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.visibility = View.VISIBLE
                            binding.contentLayout.visibility = View.GONE
                        }
                        is AccountDetailsState.Success -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.visibility = View.GONE
                            binding.contentLayout.visibility = View.VISIBLE

                            val user = state.user
                            Log.d("PROFILE", "profile = ${user?.profilePicture}")
                            user?.let {
                                binding.tvName.text = it.username
                                binding.tvEmail.text = it.email
                                binding.tvAddress.text = it.address

                                if (!it.profilePicture.isNullOrEmpty()) {
                                    binding.profileImage.setPadding(0, 0, 0, 0)
                                    binding.profileImage.background = null
                                    Glide.with(this@AccountDetailsFragment)
                                        .load(it.profilePicture)
                                        .circleCrop()
                                        .placeholder(R.drawable.ic_account)
                                        .error(R.drawable.ic_account)
                                        .into(binding.profileImage)
                                } else {
                                    val padding = (20 * resources.displayMetrics.density).toInt()
                                    binding.profileImage.setPadding(padding, padding, padding, padding)
                                    binding.profileImage.setBackgroundResource(R.drawable.circle_black)
                                    binding.profileImage.setImageResource(R.drawable.ic_account)
                                }
                            }
                        }
                        is AccountDetailsState.Error -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.visibility = View.GONE
                            binding.contentLayout.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.app_to_auth)
        }
        binding.profileImage.setOnClickListener {
            val hasProfilePicture = !viewModel.user.value?.profilePicture.isNullOrEmpty()
            val sheet = ProfilePictureBottomSheet().apply {
                arguments = Bundle().apply { putBoolean("showRemove", hasProfilePicture) }
            }
            sheet.listener = this
            sheet.show(parentFragmentManager, "ProfilePicture")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createImageUri(): Uri {
        clearOldProfilePictures()
        val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
    }

    private fun deleteImageUri() {
        clearOldProfilePictures()
    }

    private fun clearOldProfilePictures() {
        val directory = requireContext().filesDir
        directory.listFiles()?.forEach { file ->
            if (file.name.startsWith("profile_picture_") && file.name.endsWith(".jpg")) {
                file.delete()
            }
        }
    }

    override fun onRemovePictureClicked() {
        viewModel.removeUserProfilePicture()
        deleteImageUri()
    }

    override fun onCameraClicked() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "Camera permission is required to take a picture.", Toast.LENGTH_SHORT).show()
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openCamera()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
                } else {
                    showGoToSettingsDialog()
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            Log.d("Camera", "Result received. Success = $success")
            if (success) {
                Log.d("Camera", "Image Uri = ${viewModel.imageUri.value}")
                viewModel.imageUri.value?.let { uri ->
                    viewModel.saveUserProfilePicture(uri)
                }
            } else {
                Log.d("Camera", "Camera cancelled or failed")
            }
        }

    private fun openCamera() {
        val uri = createImageUri()
        viewModel.setImageUri(uri)
        Log.d("Camera", "Image Uri = $uri")
        cameraLauncher.launch(uri)
    }

    private fun showGoToSettingsDialog() {
        ConfirmationBottomSheet(
            title = "Permission Required",
            message = "Camera access was permanently denied. Please enable it in Settings to continue.",
            positiveButtonText = "Go to Settings"
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }.show(parentFragmentManager, "CameraPermissionBottomSheet")
    }

    override fun onGalleryClicked() {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val savedUri = copyImageToInternalStorage(uri)
                viewModel.saveUserProfilePicture(savedUri)
            }
        }

    private fun copyImageToInternalStorage(uri: Uri): Uri {
        clearOldProfilePictures()
        val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
    }
}