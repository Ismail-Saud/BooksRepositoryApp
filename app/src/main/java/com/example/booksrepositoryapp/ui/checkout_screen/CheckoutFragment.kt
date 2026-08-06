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
import androidx.lifecycle.repeatOnLifecycle
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
    private var hasSelectedAddress = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCheckoutState()
        setupListeners()
        setupCardValidationWatcher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupListeners() {
        val total = arguments?.getDouble("amount") ?: 0.0
        binding.btnPay.text = "Pay $%.2f".format(total)
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSelectAddress.setOnClickListener {
            findNavController().navigate(R.id.addressListFragment)
        }
        binding.rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            val isCreditCard = checkedId == binding.rbCreditCard.id
            binding.cardCreditDetails.visibility =
                if (isCreditCard) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            updatePayButton(
                arguments?.getDouble("amount") ?: 0.0,
                hasSelectedAddress
            )
        }
        binding.btnPay.setOnClickListener {
            if (binding.rbCreditCard.isChecked) {
                if (!validateCreditCardDetails()) {
                    return@setOnClickListener
                }
            }
            val loading = LoadingFragment()
            loading.show(parentFragmentManager, "loading")
            Handler(Looper.getMainLooper()).postDelayed({
                loading.dismiss()
                findNavController().navigate(R.id.successFragment)
            }, 2000)

            viewModel.clearCart()
        }
    }

    private fun observeCheckoutState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                androidx.lifecycle.Lifecycle.State.STARTED
            ) {
                viewModel.checkoutState.collect { state ->
                    when(state) {
                        CheckoutState.Idle -> {
                            hideShimmer()
                            hasSelectedAddress = false
                            binding.tvAddress.text = "Address not found"
                            updatePayButton(
                                arguments?.getDouble("amount") ?: 0.0,
                                false
                            )
                        }
                        CheckoutState.Loading -> {
                            showShimmer()
                        }
                        is CheckoutState.Success -> {
                            hideShimmer()
                            val address = state.address
                            hasSelectedAddress = address != null
                            binding.tvAddress.text = address?.fullAddress ?: "Address not found"
                            updatePayButton(
                                arguments?.getDouble("amount") ?: 0.0,
                                hasSelectedAddress
                            )
                        }
                        is CheckoutState.Error -> {
                            hideShimmer()
                            Toast.makeText(requireContext(),state.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateCreditCardDetails(): Boolean {
        val cardNumber = binding.etCardNumber.text.toString()
        val holderName = binding.etCardHolder.text.toString()
        val expiry = binding.etExpiry.text.toString()
        val cvv = binding.etCVV.text.toString()
        binding.tilCardNumber.error = null
        binding.tilCardHolder.error = null
        binding.etExpiry.error = null
        binding.etCVV.error = null
        var isValid = true
        if (!viewModel.isValidCardNumber(cardNumber)) {
            binding.tilCardNumber.error = "Invalid card number"
            isValid = false
        }
        if (!viewModel.isValidCardHolderName(holderName)) {
            binding.tilCardHolder.error = "Invalid card holder name"
            isValid = false
        }
        if (!viewModel.isValidExpiryDate(expiry)) {
            binding.etExpiry.error = "Invalid expiry date"
            isValid = false
        }
        if (!viewModel.isValidCVV(cvv)) {
            binding.etCVV.error = "Invalid CVV"
            isValid = false
        }
        return isValid
    }

    private fun isCreditCardDetailsValid(): Boolean {
        val cardNumber = binding.etCardNumber.text.toString()
        val holderName = binding.etCardHolder.text.toString()
        val expiry = binding.etExpiry.text.toString()
        val cvv = binding.etCVV.text.toString()
        return viewModel.isValidCardNumber(cardNumber) && viewModel.isValidCardHolderName(holderName) &&
                viewModel.isValidExpiryDate(expiry) &&
                viewModel.isValidCVV(cvv)
    }

    private fun updatePayButton(total: Double, hasSelectedAddress: Boolean) {
        binding.btnPay.text = "Pay $%.2f".format(total)
        val enabled = total > 0.0 && hasSelectedAddress &&
                (!binding.rbCreditCard.isChecked || isCreditCardDetailsValid())
        binding.btnPay.isEnabled = enabled
        if (enabled) {
            binding.btnPay.alpha = 1f
            binding.btnPay.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
        } else {
            binding.btnPay.alpha = 0.5f
            binding.btnPay.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.gray)
            )
        }
    }

    private fun setupCardValidationWatcher() {
        val watcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                updatePayButton(
                    arguments?.getDouble("amount") ?: 0.0,
                    hasSelectedAddress
                )
            }
            override fun afterTextChanged(
                s: android.text.Editable?
            ) {}
        }
        binding.etCardNumber.addTextChangedListener(watcher)
        binding.etCardHolder.addTextChangedListener(watcher)
        binding.etExpiry.addTextChangedListener(watcher)
        binding.etCVV.addTextChangedListener(watcher)
    }

    private fun showShimmer() {
        binding.shimmerCheckout.visibility = View.VISIBLE
        binding.scrollContent.visibility = View.GONE
        binding.btnPay.visibility = View.GONE
        binding.shimmerCheckout.startShimmer()
    }
    private fun hideShimmer() {
        binding.shimmerCheckout.stopShimmer()
        binding.shimmerCheckout.visibility = View.GONE
        binding.scrollContent.visibility = View.VISIBLE
        binding.btnPay.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}