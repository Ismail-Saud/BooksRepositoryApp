package com.example.booksrepositoryapp.ui.auth.getstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentGetStartedBinding
import com.example.booksrepositoryapp.databinding.FragmentLandingPageBinding
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedViewModel

class GetStartedFragment : Fragment(R.layout.fragment_get_started) {
    private var _binding: FragmentGetStartedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GetStartedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}