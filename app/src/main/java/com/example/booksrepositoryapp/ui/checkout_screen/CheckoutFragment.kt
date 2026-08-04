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
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
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
    private lateinit var addressAdapter: AddressAdapter
    private var selectedAddress: AddressModel? = null
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setupRecycler()
        observeAddresses()
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

    private var addressBeingLocated: AddressModel? = null

    private fun checkLocationPermission(address: AddressModel) {
        addressBeingLocated = address
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> getCurrentLocation()

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(requireContext(), "Location permission is required to get your current address.", Toast.LENGTH_LONG).show()
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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
        val target = addressBeingLocated ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            val locationAddress = withContext(Dispatchers.IO) {
                try {
                    Geocoder(requireContext(), Locale.getDefault())
                        .getFromLocation(latitude, longitude, 1)?.firstOrNull()
                } catch (e: Exception) { null }
            }
            if (locationAddress == null) {
                Toast.makeText(requireContext(), "Unable to get address", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val updated = target.copy(
                house = locationAddress.featureName ?: "",
                street = locationAddress.thoroughfare,
                area = locationAddress.subLocality ?: "",
                city = locationAddress.locality ?: locationAddress.subAdminArea ?: "",
                postalCode = locationAddress.postalCode,
                country = locationAddress.countryName ?: "",
                fullAddress = locationAddress.getAddressLine(0) ?: "",
                latitude = latitude,
                longitude = longitude
            )
            viewModel.updateAddress(updated)
            addressBeingLocated = null
            Toast.makeText(requireContext(), "Address updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationFromAddress(
        address: AddressModel,
        fullAddress: String
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val location = withContext(Dispatchers.IO) {
                try {
                    Geocoder(requireContext(), Locale.getDefault())
                        .getFromLocationName(fullAddress, 1)
                        ?.firstOrNull()
                } catch (e: IOException) {
                    null
                }
            }

            if (location == null) {
                Toast.makeText(requireContext(), "Address not found", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val updatedAddress = address.copy(
                fullAddress = fullAddress,
                latitude = location.latitude,
                longitude = location.longitude
            )

            viewModel.updateAddress(updatedAddress)

            Toast.makeText(
                requireContext(),
                "Address updated successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
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
        updatePayButton(total)
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddAddress.setOnClickListener {
            val newAddress = AddressModel(
                userId = viewModel.userId,
                house = "",
                street = null,
                area = "",
                city = "",
                postalCode = null,
                country = "",
                fullAddress = "No delivery address selected",
                latitude = 0.0,
                longitude = 0.0
            )
            viewModel.addAddress(newAddress)
        }
        binding.btnDeleteAddresses.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.deleteAddress()
            }
        }
        binding.btnPay.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select a delivery address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val loading = LoadingFragment()
            loading.show(fragmentManager, "loading")
            Handler(Looper.getMainLooper()).postDelayed({
                loading.dismiss()
                findNavController().navigate(R.id.successFragment)
            }, 2000)
            viewModel.clearCart()
        }
    }

    private fun updatePayButton(total: Double) {
        binding.btnPay.text = "Pay $%.2f".format(total)
        val enabled = total > 0.0
        binding.btnPay.isEnabled = enabled
        binding.btnPay.alpha = if (enabled) 1f else 0.5f
    }

    private fun observeAddresses() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addresses.collect { addresses ->
                addressAdapter.submitList(addresses)
                if (addresses.isNotEmpty()) {
                    if (selectedAddress == null) {
                        selectedAddress = addresses.first()
                    } else {
                        selectedAddress = addresses.firstOrNull {
                            it.id == selectedAddress?.id
                        } ?: addresses.first()
                    }
                } else {
                    selectedAddress = null
                }
                updatePayButton(
                    arguments?.getDouble("amount") ?: 0.0
                )
            }
        }
    }

    private fun setupRecycler() {
        addressAdapter = AddressAdapter(
            onLocationClick = { address ->
                checkLocationPermission(address)
            },
            onCheckClick = { address, fullAddress ->
                getLocationFromAddress(address, fullAddress)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateAddress(address)
                }
                selectedAddress = address
                Toast.makeText(
                    requireContext(),
                    "Selected Address $address",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onSelectAddress = { address ->
                selectedAddress = address
                Toast.makeText(
                    requireContext(),
                    "Selected Address $address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.rvAddresses.adapter = addressAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}