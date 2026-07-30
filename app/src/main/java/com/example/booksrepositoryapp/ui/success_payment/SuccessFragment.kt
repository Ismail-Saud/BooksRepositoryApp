package com.example.booksrepositoryapp.ui.success_payment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.booksrepositoryapp.MainActivity
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentSuccessBinding
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment
import com.example.booksrepositoryapp.ui.utils.NavigationUtil

class PaymentSuccessFragment : Fragment() {
    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigator: NavigationUtil

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
        navigator = NavigationUtil(parentFragmentManager)
        binding.btnClose.setOnClickListener {
            navigator.navigateAsRoot(BooksCategoryFragment())
            (requireActivity() as MainActivity)
                .updateSelectedBottomNav(R.id.nav_home)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
            navigator.navigateAsRoot(BooksCategoryFragment())
            (requireActivity() as MainActivity)
                .updateSelectedBottomNav(R.id.nav_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}