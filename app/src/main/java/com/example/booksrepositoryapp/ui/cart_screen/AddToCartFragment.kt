package com.example.booksrepositoryapp.ui.cart_screen

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.databinding.FragmentAddToCartBinding
import com.example.booksrepositoryapp.ui.address_screen.AddressShimmerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class AddToCartFragment : Fragment(R.layout.fragment_add_to_cart) {

    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartShimmerAdapter: AddToCartShimmerAdapter
    private lateinit var bundle: Bundle
    private val viewModel: AddToCartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupRecyclerView()
        viewModel.getCartItems()
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() {
        cartShimmerAdapter = AddToCartShimmerAdapter()
        cartAdapter = CartAdapter(
            onIncreaseClick = { cartItem ->
                viewModel.increaseQuantity(cartItem)
            },
            onDecreaseClick = { cartItem ->
                if (cartItem.quantity > 1) {
                    viewModel.decreaseQuantity(cartItem)
                }
                else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Remove Item")
                        .setMessage("Remove this item from your cart?")
                        .setPositiveButton("Remove") { _,_ ->
                            viewModel.removeCartItem(cartItem)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            },
            onRemoveClick = { cartItem ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Remove Item")
                    .setMessage("Remove this item from your cart?")
                    .setPositiveButton("Remove") { _,_ ->
                        viewModel.removeCartItem(cartItem)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
            isNestedScrollingEnabled = true
            itemAnimator = null
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addToCartState.collect { state ->
                    when (state) {
                        AddToCartState.Idle -> {}
                        AddToCartState.Loading -> {}
                        is AddToCartState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is AddToCartState.Success -> {
                            cartAdapter.submitList(state.cartItem)
                            binding.rvCartItems.adapter = cartAdapter
                            updateSummary(state.cartItem)
                            binding.btnCheckout.isEnabled = state.cartItem.isNotEmpty()
                            if (state.cartItem.isEmpty()) {
                                binding.btnCheckout.alpha = 0.5f
                            } else {
                                binding.btnCheckout.alpha = 1f
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateSummary(cartItem: List<CartItem>) {
        val subTotal = cartItem.sumOf {
            it.price* it.quantity
        }
        val shipping = if (cartItem.isEmpty()) 0.0 else 10.0
        val total = subTotal + shipping
        binding.tvSubtotalAmount.text = "$%.2f".format(subTotal)
        binding.tvShippingAmount.text = "$%.2f".format(shipping)
        binding.tvTotalAmount.text = "$%.2f".format(total)
        bundle = Bundle().apply {
            putDouble("amount", total)
        }
    }

    private fun setupListeners() {
        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(
                R.id.add_to_cart_to_checkout,
                bundle
            )
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}