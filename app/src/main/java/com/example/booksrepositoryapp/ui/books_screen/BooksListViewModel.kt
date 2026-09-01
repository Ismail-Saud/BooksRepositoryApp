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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BooksListViewModel(application: Application) : AndroidViewModel(application) {
    private val _bookState = MutableStateFlow<BooksListState>(BooksListState.Idle)
    val bookState = _bookState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private var allBooks: List<BookDetailsModel> = emptyList()
    private var minPrice = 0
    private var maxPrice = Int.MAX_VALUE

    private val bookRepo = BooksRepository(application)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBooksByCategory(subject: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _bookState.value = BooksListState.Loading
                when (val result = bookRepo.refreshBooks(subject)) {
                    RefreshResult.Offline -> {
                        _bookState.value = BooksListState.Offline
                    }
                    is RefreshResult.Error -> {
                        _bookState.value = BooksListState.Error(result.message)
                    }
                    RefreshResult.Success -> {}
                }
                bookRepo.getBooks(subject).collect { books ->
                    allBooks = books
                    if (books.isNotEmpty()) {
                        applyFilters()
                    }
                }
            }
        }
    }

    fun filterByPrice(min: Int, max: Int) {
        minPrice = min
        maxPrice = max
        applyFilters()
    }

    fun searchBooks(query: String) {
        _searchQuery.value = query
    }

    private fun applyFilters() {
        val query = _searchQuery.value
        val result = allBooks.filter { book ->
            val matchesSearch = query.isEmpty() ||
                        book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true)
            val matchesPrice = book.price?.toInt() in minPrice..maxPrice
            matchesSearch && matchesPrice
        }
        _bookState.value = BooksListState.Success(result)
    }

    fun resetState() {
        _searchQuery.value = ""
        minPrice = 0
        maxPrice = Int.MAX_VALUE
        _bookState.value = BooksListState.Idle
    }

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .drop(1)
                .collectLatest {
                    applyFilters()
                }
        }
    }
}