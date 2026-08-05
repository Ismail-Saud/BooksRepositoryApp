package com.example.booksrepositoryapp.ui.address_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.databinding.FragmentAddressListBinding
import com.example.booksrepositoryapp.databinding.FragmentCheckoutBinding
import com.example.booksrepositoryapp.ui.checkout_screen.AddressAdapter
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class AddressListFragment : Fragment(R.layout.fragment_address_list) {
    private var _binding: FragmentAddressListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddressListViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addressAdapter: AddressAdapter
    companion object {
        private const val MAX_ADDRESSES = 5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setupRecycler()
        observeAllAddresses()
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
                house = location.subThoroughfare ?: "",
                street = location.thoroughfare,
                area = location.subLocality ?: location.featureName ?: "",
                city = location.locality ?: location.subAdminArea ?: "",
                postalCode = location.postalCode,
                country = location.countryName ?: "",
                fullAddress = fullAddress,
                latitude = location.latitude,
                longitude = location.longitude
            )

            viewModel.updateAddress(updatedAddress)
            viewModel.updateSelectedAddress(updatedAddress.id)

            Toast.makeText(
                requireContext(),
                "Address updated successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupRecycler() {
        addressAdapter = AddressAdapter(
            onLocationClick = { address ->
                checkLocationPermission(address)
            },
            onCheckClick = { address, fullAddress ->
                getLocationFromAddress(address, fullAddress)
            },
            onDeleteClick = { address ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteAddress(address)
                }
            }
        )
        binding.rvAddresses.adapter = addressAdapter
    }
    private fun observeAllAddresses() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addresses.collect { addresses ->
                addressAdapter.submitList(addresses)
                binding.btnAddAddress.isEnabled = addresses.size < MAX_ADDRESSES
                if (addresses.size >= MAX_ADDRESSES) {
                    binding.btnAddAddress.alpha = 0.5f
                } else {
                    binding.btnAddAddress.alpha = 1f
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddAddress.setOnClickListener {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}