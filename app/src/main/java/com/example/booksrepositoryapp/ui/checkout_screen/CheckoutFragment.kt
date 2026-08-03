package com.example.booksrepositoryapp.ui.checkout_screen

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.DialogueAddressBinding
import com.example.booksrepositoryapp.databinding.FragmentCheckoutBinding
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.example.booksrepositoryapp.ui.loading_screen.LoadingFragment
import com.example.booksrepositoryapp.ui.success_payment.PaymentSuccessFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import kotlin.toString

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setupListeners()
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                showGoToSettingsDialog()
            }
        }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(requireContext(), "Location permission is required to get your current address.", Toast.LENGTH_LONG).show()
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
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

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                getAddressFromLocation(latitude, longitude)
                Log.d("Location", "Lat: $latitude\nLng: $longitude")
                Toast.makeText(
                    requireContext(),
                    "Lat: $latitude\nLng: $longitude",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Unable to get current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Failed to get location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            val address = withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(
                        requireContext(),
                        Locale.getDefault()
                    )
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    addresses?.firstOrNull()?.getAddressLine(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
            address?.let { binding.tvAddress.text = it
                Toast.makeText(
                    requireContext(),
                    "Address Found",
                    Toast.LENGTH_SHORT
                ).show()
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "Unable to get address",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.saveAddress(address.toString())
        }
    }

    private fun getLocationFromAddress(address: String) {
        val geocoder = Geocoder(
            requireContext(),
            Locale.getDefault()
        )
        try {
            val locations = geocoder.getFromLocationName(address, 1)
            if (!locations.isNullOrEmpty()) {
                val location = locations[0]
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("Location", "Lat: $latitude\nLng: $longitude")
                Toast.makeText(
                    requireContext(),
                    "Lat: $latitude\nLng: $longitude",
                    Toast.LENGTH_LONG
                ).show()
                val address = location.getAddressLine(0)
                binding.tvAddress.text = address
                viewModel.saveAddress(address.toString())
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "Address not found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Unable to find address",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun showAddressDialog() {
        val dialogBinding = DialogueAddressBinding.inflate(layoutInflater)
        AlertDialog.Builder(requireContext())
            .setTitle("Change Address")
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { dialog, _ ->
                val fullAddress = with(dialogBinding) {
                    "${etHouse.text}, ${etStreet.text}, ${etArea.text}, ${etCity.text}, ${etPostal.text}, ${etCountry.text}"
                }
                if (fullAddress.isNotBlank()) {
                    getLocationFromAddress(fullAddress)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupListeners() {
        val fragmentManager = parentFragmentManager
        val total = arguments?.getDouble("amount") ?: 0.0
        binding.btnPay.text = "Pay $%.2f".format(total)
        viewLifecycleOwner.lifecycleScope.launch {
            val address = viewModel.getAddress()
            binding.tvAddress.text = address ?: "No Delivery Address Added"
            updatePayButton(total)
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCurrentLocation.setOnClickListener {
            checkLocationPermission()
        }
        binding.tvChange.setOnClickListener {
            showAddressDialog()
        }
        binding.btnPay.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val address = viewModel.getAddress()
                if (address == null || total <= 0.0) return@launch
                val loading = LoadingFragment()
                loading.show(fragmentManager, "loading")
                Handler(Looper.getMainLooper()).postDelayed({
                    loading.dismiss()
                    findNavController().navigate(R.id.successFragment)
                }, 2000)
                viewModel.clearCart()
            }
        }
    }

    private suspend fun updatePayButton(total: Double) {
        val hasAddress = viewModel.getAddress() != null
        val enabled = total > 0.0 && hasAddress

        binding.btnPay.isEnabled = enabled
        binding.btnPay.alpha = if (enabled) 1f else 0.5f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}