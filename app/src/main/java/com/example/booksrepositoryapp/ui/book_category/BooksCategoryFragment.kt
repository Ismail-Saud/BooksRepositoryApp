package com.example.booksrepositoryapp.ui.book_category

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.api.models.Category
import com.example.booksrepositoryapp.data.api.models.categories
import com.example.booksrepositoryapp.databinding.FragmentBooksCategoryBinding
import com.example.booksrepositoryapp.databinding.FragmentRegisterBinding
import com.example.booksrepositoryapp.ui.books_page.BooksListFragment
import com.example.booksrepositoryapp.ui.landingpage.LandingPageFragment

class BooksCategoryFragment : Fragment(R.layout.fragment_books_category) {

    private var _binding: FragmentBooksCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter

    private val viewModel: BooksCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
        setupRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupObservers() {
        viewModel.categoryState.observe(
            viewLifecycleOwner) { state ->
                when (state) {
                    BooksCategoryState.Idle -> {}
                    BooksCategoryState.Loading -> {}
                    is BooksCategoryState.Success -> {
                        adapter.submitList(state.categories)
                        binding.recyclerView.scrollToPosition(0)
                    }
                    is BooksCategoryState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.resetState()
                    }
                }
            }
    }

    private fun setupListeners() {
        binding.etSearch.addTextChangedListener {
            viewModel.searchTodos(
                it.toString().trim()
            )
        }
        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            parentFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer, LandingPageFragment()
            ).addToBackStack(null).commit()
        }
    }

    private fun setupRecyclerView() {
        adapter = CategoryAdapter { category ->
            val fragment = BooksListFragment()
            val bundle = Bundle().apply {
                putString("subject", category.apiValue)
                putString("title", category.title)
            }

            fragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer, fragment)
                    .addToBackStack(null).commit()
        }


        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        adapter.submitList(categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}