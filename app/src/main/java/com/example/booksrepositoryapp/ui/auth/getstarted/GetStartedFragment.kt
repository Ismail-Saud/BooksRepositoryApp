package com.example.booksrepositoryapp.ui.auth.getstarted

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentGetStartedBinding
import kotlinx.coroutines.launch

class GetStartedFragment : Fragment(R.layout.fragment_get_started) {
    private var _binding: FragmentGetStartedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GetStartedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getStartedState.collect { state ->
                    when(state) {
                        GetStartedState.Idle -> {}
                        is GetStartedState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                        GetStartedState.Success -> {
                            findNavController().navigate(R.id.getStarted_to_app)
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(
                R.id.get_started_to_register
            )
        }
        binding.btnGetStarted.setOnClickListener {
            lifecycleScope.launch {
                viewModel.login(
                    email = binding.etUsername.text.toString(),
                    password = binding.etPassword.text.toString()
                )
            }
        }
    }
}