package com.example.booksrepositoryapp.ui.books_screen

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentBooksListBinding
import com.example.booksrepositoryapp.ui.book_details.BookDetailsFragment
import com.example.booksrepositoryapp.ui.shimmer.BookListShimmerAdapter
import kotlinx.coroutines.launch

class BooksListFragment : Fragment() {

    private var _binding: FragmentBooksListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BooksAdapter
    private val viewModel: BooksListViewModel by viewModels()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subject = arguments?.getString("subject") ?: "Unknown"
        viewModel.getBooksByCategory(subject)
        setupRecyclerView()
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookState.collect { state ->
                when (state) {
                    BooksListState.Idle -> {}
                    BooksListState.Loading -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.booksRecyclerView.visibility = View.GONE
                    }
                    is BooksListState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.resetState()
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.booksRecyclerView.visibility = View.VISIBLE
                    }
                    is BooksListState.Success -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.booksRecyclerView.visibility = View.VISIBLE
                        adapter.submitList(state.books)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val subject = arguments?.getString("title") ?: "Unknown"
        adapter = BooksAdapter(subject.replaceFirstChar { it.uppercase() }) { work ->
            Toast.makeText(
                requireContext(), "Navigated", Toast.LENGTH_SHORT
            ).show()
            Log.d("BOOK", "Cover ID before navigation = ${work.coverId}")

            val bundle = Bundle().apply {
                putString("key", work.workId)
            }
            findNavController().navigate(
                R.id.action_booksList_to_bookDetails,
                bundle
            )
        }
        binding.shimmerRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.shimmerRecyclerView.adapter = BookListShimmerAdapter()
        binding.booksRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.booksRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}