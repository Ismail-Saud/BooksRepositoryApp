package com.example.booksrepositoryapp.ui.book_details

import android.R.attr.onClick
import android.graphics.drawable.Drawable
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.databinding.FragmentBookDetailsBinding
import com.example.booksrepositoryapp.databinding.FragmentBooksListBinding
import com.example.booksrepositoryapp.ui.books_screen.BooksListState
import kotlinx.coroutines.launch
import kotlin.random.Random

class BookDetailsFragment : Fragment() {
    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = arguments?.getString("key") ?: "Unknown"
        viewModel.getBookDetails(key)
        setupObservers()
        setupBackBtn()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.bookDetailState.collect { state ->
                when (state) {
                    BooksListState.Idle -> {}
                    is BookDetailsState.Loading -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.contentLayout.visibility = View.GONE
                    }
                    is BookDetailsState.Success -> {

                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.contentLayout.visibility = View.VISIBLE

                        val book = state.books

                        binding.tvBookTitle.text = book.title
                        binding.tvDescription.text = book.description
                        binding.tvCategory.text = book.category.replaceFirstChar { it.uppercaseChar() }
                        binding.tvGenre.text = "Category: ${book.category}"
                        binding.tvAuthor.text = "Author: ${book.author}"
                        binding.tvPrice.text = "$${book.price}"
                        binding.tvRating.text = "Rating: ${book.rating}/5"
                        if (book.coverId != 0) {
                            binding.progressBar.visibility = View.VISIBLE
                            Glide.with(this@BookDetailsFragment)
                                .load("https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg")
                                .error(R.drawable.book_cover_img)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        p0: GlideException?,
                                        p1: Any?,
                                        p2: Target<Drawable?>,
                                        p3: Boolean
                                    ): Boolean {
                                        binding.progressBar.visibility = View.GONE
                                        return false
                                    }
                                    override fun onResourceReady(
                                        p0: Drawable,
                                        p1: Any,
                                        p2: Target<Drawable?>?,
                                        p3: DataSource,
                                        p4: Boolean
                                    ): Boolean {
                                        binding.progressBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(binding.imgBook)
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.imgBook.setImageResource(R.drawable.book_cover_img)
                        }
                    }
                    is BookDetailsState.Error -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.contentLayout.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.resetState()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun generateAmount(): Double {
        return String.format("%.2f", Random.nextDouble(3.0, 4.99)).toDouble()
    }

    private fun setupBackBtn() {
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}