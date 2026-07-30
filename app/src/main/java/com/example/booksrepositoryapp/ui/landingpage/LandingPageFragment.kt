package com.example.booksrepositoryapp.ui.landingpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentLandingPageBinding
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedFragment
import com.example.booksrepositoryapp.ui.auth.register.RegisterFragment
import com.example.booksrepositoryapp.ui.utils.NavigationUtil

class LandingPageFragment : Fragment(R.layout.fragment_landing_page) {

    private var _binding: FragmentLandingPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigator: NavigationUtil

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
        navigator = NavigationUtil(parentFragmentManager)
        binding.getStartedBtn.setOnClickListener {
            navigator.navigateTo(GetStartedFragment())
        }

        binding.registerBtn.setOnClickListener {
            navigator.navigateTo(RegisterFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}