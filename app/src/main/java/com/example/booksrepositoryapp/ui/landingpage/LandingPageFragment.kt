package com.example.booksrepositoryapp.ui.landingpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentLandingPageBinding
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedFragment
import com.example.booksrepositoryapp.ui.auth.register.RegisterFragment

class LandingPageFragment : Fragment(R.layout.fragment_landing_page) {

    private var _binding: FragmentLandingPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupListeners () {
        binding.getStartedBtn.setOnClickListener {
            findNavController().navigate(
                R.id.landing_to_get_started
            )
        }

        binding.registerBtn.setOnClickListener {
            findNavController().navigate(
                R.id.landing_to_register
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}