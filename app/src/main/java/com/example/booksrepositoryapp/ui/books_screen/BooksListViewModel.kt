package com.example.booksrepositoryapp.ui.books_screen

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.api.refreshResult.RefreshResult
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.data.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksListViewModel(application: Application) : AndroidViewModel(application) {
    private val _bookState = MutableStateFlow<BooksListState>(BooksListState.Idle)
    val bookState = _bookState.asStateFlow()
    private var allBooks: List<BookDetailsModel> = emptyList()
    private var activeSearch = ""
    private var minPrice = 0
    private var maxPrice = Int.MAX_VALUE

    private val bookRepo = BooksRepository(application)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBooksByCategory(subject: String) {
        viewModelScope.launch {
            _bookState.value = BooksListState.Loading
            when (val result = bookRepo.refreshBooks(subject)) {
                RefreshResult.Offline -> {
                    _bookState.value = BooksListState.Offline
                }
                is RefreshResult.Error -> {
                    _bookState.value =
                        BooksListState.Error(result.message)
                }
                else -> {}
            }
            bookRepo.getBooks(subject).collect { books ->
                allBooks = books
                applyFilters()
            }
        }
    }

    fun filterByPrice(min: Int, max: Int) {
        minPrice = min
        maxPrice = max
        applyFilters()
    }

    fun searchBooks(query: String) {
        activeSearch = query
        applyFilters()
    }

    private fun applyFilters() {
        val result = allBooks.filter { book ->
            val matchesSearch = activeSearch.isEmpty() ||
                    book.title.contains(activeSearch, ignoreCase = true) ||
                    book.author.contains(activeSearch, ignoreCase = true)
            val matchesPrice = book.price?.toInt() in (minPrice..maxPrice)
            matchesSearch && matchesPrice
        }
        _bookState.value = BooksListState.Success(result)
    }


    fun resetState() {
        activeSearch = ""
        minPrice = 0
        maxPrice = Int.MAX_VALUE
        _bookState.value = BooksListState.Idle
    }
}