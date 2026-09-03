package com.example.booksrepositoryapp.ui.address_screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.firebase.firestore.AddressModelFB
import com.example.booksrepositoryapp.databinding.FragmentAddressListBinding
import com.example.booksrepositoryapp.helper.LocationHelper
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import kotlinx.coroutines.launch

class AddressListFragment : Fragment(R.layout.fragment_address_list) {
    private var _binding: FragmentAddressListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddressListViewModel by viewModels()
    private lateinit var locationHelper: LocationHelper
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressShimmerAdapter: AddressShimmerAdapter
    companion object {
        private const val MAX_ADDRESSES = 5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationHelper = LocationHelper(requireContext())
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

    private var addressBeingLocated: AddressModelFB? = null

    private fun checkLocationPermission(address: AddressModelFB) {
        addressBeingLocated = address
        when {
            locationHelper.hasLocationPermission() -> {
                getCurrentLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(
                    requireContext(),
                    "Location permission is required to get your current address.",
                    Toast.LENGTH_LONG
                ).show()
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            else -> {
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun showGoToSettingsDialog() {
        ConfirmationBottomSheet(
            title = "Permission Required",
            message = "Location access was permanently denied. Please enable it in Settings to continue.",
            positiveButtonText = "Go to Settings"
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }.show(parentFragmentManager, "CameraPermissionBottomSheet")
    }

    private fun getCurrentLocation() {
        locationHelper.getCurrentLocation(
            onSuccess = { location ->
                if (location != null) {
                    getAddressFromLocation(
                        location.latitude,
                        location.longitude
                    )
                    Log.d(
                        "Location",
                        "Lat: ${location.latitude}\nLng: ${location.longitude}"
                    )
                } else {
                    addressBeingLocated?.let {
                        viewModel.setFetchingLocation(it.id, false)
                    }
                    Toast.makeText(
                        requireContext(),
                        "Unable to get current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onFailure = {
                addressBeingLocated?.let {
                    viewModel.setFetchingLocation(it.id, false)
                }
                Toast.makeText(
                    requireContext(),
                    "Failed to get location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val target = addressBeingLocated ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            val locationAddress = locationHelper.getAddressFromLocation(
                    latitude,
                    longitude
                )
            if (locationAddress == null) {
                viewModel.setFetchingLocation(target.id, false)
                Toast.makeText(
                    requireContext(),
                    "Unable to get address",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val updated = target.copy(
                house = locationAddress.featureName ?: "",
                street = locationAddress.thoroughfare ?: "",
                area = locationAddress.subLocality ?: "",
                city = locationAddress.locality ?: locationAddress.subAdminArea ?: "",
                postalCode = locationAddress.postalCode ?: "",
                country = locationAddress.countryName ?: "",
                fullAddress = locationAddress.getAddressLine(0) ?: "",
                latitude = latitude,
                longitude = longitude,
                isSelected = true
            )
            viewModel.updateAddress(updated)
            viewModel.setFetchingLocation(target.id, false)
            addressBeingLocated = null
            Toast.makeText(
                requireContext(),
                "Address updated",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getLocationFromAddress(address: AddressModelFB, fullAddress: String) {
        viewModel.setSaving(address.id, true)
        viewLifecycleOwner.lifecycleScope.launch {
            val location = locationHelper.getLocationFromAddress(fullAddress)
            if (location == null) {
                viewModel.setSaving(address.id, false)
                Toast.makeText(
                    requireContext(),
                    "Address not found",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            val updatedAddress = address.copy(
                house = location.subThoroughfare ?: "",
                street = location.thoroughfare ?: "",
                area = location.subLocality ?: location.featureName ?: "",
                city = location.locality ?: location.subAdminArea ?: "",
                postalCode = location.postalCode ?: "",
                country = location.countryName ?: "",
                fullAddress = fullAddress,
                latitude = location.latitude,
                longitude = location.longitude
            )
            viewModel.updateAddress(updatedAddress)
            viewModel.updateSelectedAddress(updatedAddress.id)
            viewModel.setSaving(address.id, false)
            Toast.makeText(
                requireContext(),
                "Address updated successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupRecycler() {
        addressShimmerAdapter = AddressShimmerAdapter()
        addressAdapter = AddressAdapter(
            onLocationClick = { address ->
                viewModel.setFetchingLocation(address.id, true)
                checkLocationPermission(address)
            },
            onCheckClick = { address, fullAddress ->
                getLocationFromAddress(address, fullAddress)
            },
            onDeleteClick = { address ->
                ConfirmationBottomSheet(
                    title = "Delete Address?",
                    message = "Are you sure you want to delete the address? \n This action cannot be undone",
                    positiveButtonText = "Delete"
                ) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.deleteAddress(address)
                    }
                }.show(parentFragmentManager, "Confirmation")
            }
        )
        binding.rvAddresses.adapter = addressShimmerAdapter
    }

    private fun observeAllAddresses() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addresses.collect { addresses ->
                addressAdapter.submitList(addresses)
                binding.rvAddresses.adapter = addressAdapter
                binding.btnAddAddress.isEnabled = addresses.size < MAX_ADDRESSES
                if (addresses.size >= MAX_ADDRESSES) {
                    binding.btnAddAddress.alpha = 0.5f
                } else {
                    binding.btnAddAddress.alpha = 1f
                }
                binding.btnDeleteAddresses.isEnabled = addresses.isNotEmpty()
                binding.btnDeleteAddresses.alpha = if (addresses.isNotEmpty()) 1f else 0.5f
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddAddress.setOnClickListener {
            val newAddress = AddressModelFB(
                house = "",
                street = "",
                area = "",
                city = "",
                postalCode = "",
                country = "",
                fullAddress = "",
                latitude = 0.0,
                longitude = 0.0,
                isSelected = false
            )
            viewModel.addAddress(newAddress)
        }
        binding.btnDeleteAddresses.setOnClickListener {
            ConfirmationBottomSheet(
                title = "Delete All Addresses?",
                message = "Are you sure you want to delete all addresses? \n This action cannot be undone",
                positiveButtonText = "Delete"
            ) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteAllAddresses()
                }
            }.show(parentFragmentManager, "Confirmation")
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
