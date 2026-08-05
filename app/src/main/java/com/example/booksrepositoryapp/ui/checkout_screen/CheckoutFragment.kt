package com.example.booksrepositoryapp.ui.checkout_screen

import android.Manifest
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.databinding.FragmentCheckoutBinding
import com.example.booksrepositoryapp.ui.address_screen.AddressListFragment
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.example.booksrepositoryapp.ui.loading_screen.LoadingFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAddresses()
        setupListeners()
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
        binding.btnSelectAddress.setOnClickListener {
            findNavController().navigate(R.id.addressListFragment)
        }
        binding.cardCreditDetails.visibility =
            if (binding.rbCreditCard.isChecked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        binding.rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            binding.cardCreditDetails.visibility =
                if (checkedId == binding.rbCreditCard.id) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        binding.btnPay.setOnClickListener {
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
            viewModel.address.collect { address ->
                updatePayButton(
                    arguments?.getDouble("amount") ?: 0.0
                )
                binding.tvAddress.text = address?.fullAddress ?: "Address not found"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}