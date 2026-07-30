package com.example.booksrepositoryapp.ui.auth.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentRegisterBinding
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedFragment
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment
import com.example.booksrepositoryapp.ui.landingpage.LandingPageFragment
import com.example.booksrepositoryapp.ui.utils.NavigationUtil
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigator: NavigationUtil
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupObservers() {
        navigator = NavigationUtil(parentFragmentManager)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUser.collect { state ->
                    when (state) {
                        RegisterState.Idle -> {}
                        is RegisterState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                        RegisterState.Success -> {
                            navigator.navigateAsRoot(BooksCategoryFragment())
                        }
                    }
                }
            }
        }

    }

    private fun setupListeners() {
        navigator = NavigationUtil(parentFragmentManager)
        binding.btnBack.setOnClickListener {
            navigator.navigateAsRoot(LandingPageFragment())
        }
        binding.tvSignIn.setOnClickListener {
            navigator.replace(GetStartedFragment())
        }
        binding.btnRegister.setOnClickListener {
            lifecycleScope.launch {
                viewModel.register(
                    userName = binding.etUsername.text?.trim().toString(),
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString(),
                    confirmPass = binding.etConfirmPassword.text.toString()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}