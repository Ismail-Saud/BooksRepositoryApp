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
    private val bookRepo = BooksRepository(application)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBooksByCategory(subject: String) {
        viewModelScope.launch {
            when (val result = bookRepo.refreshBooks(subject)) {
                RefreshResult.Offline -> {
                    _bookState.value = BooksListState.Offline
                }
                is RefreshResult.Error -> {
                    _bookState.value = BooksListState.Error(result.message)
                    Log.d("Api Error", result.message)
                }
                else -> {}
            }

            bookRepo.getBooks(subject).collect { books ->
                allBooks = books
                _bookState.value = BooksListState.Success(books)
            }
        }
    }

    fun searchTodos (query: String) {
        activeSearch = query
        viewModelScope.launch {
            val searchResult = allBooks
            val result = if (activeSearch.isEmpty()) {
                searchResult
            } else {
                searchResult.filter { book ->
                    book.title.contains(activeSearch, ignoreCase = true) ||
                    book.author.contains(activeSearch, ignoreCase = true)
                }
            }
            _bookState.value = BooksListState.Success(result)
        }
    }

    fun resetState () {
        _bookState.value = BooksListState.Idle
    }
}