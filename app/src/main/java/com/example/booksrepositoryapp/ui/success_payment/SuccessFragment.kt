package com.example.booksrepositoryapp.ui.success_payment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.booksrepositoryapp.MainActivity
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentSuccessBinding
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment

class PaymentSuccessFragment : Fragment() {
    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener {
            goToHome()
            (requireActivity() as MainActivity)
                .selectBottomNavItem(R.id.nav_home)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
            goToHome()
            (requireActivity() as MainActivity)
                .selectBottomNavItem(R.id.nav_home)
        }
    }

    private fun goToHome(){
        findNavController().navigate(
            R.id.home_graph,
            null,
            NavOptions.Builder()
                .setPopUpTo(
                    R.id.cart_graph,
                    true
                )
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}