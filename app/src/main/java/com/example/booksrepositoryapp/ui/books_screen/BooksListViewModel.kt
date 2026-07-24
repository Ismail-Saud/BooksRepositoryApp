package com.example.booksrepositoryapp.ui.books_screen

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksListViewModel(application: Application) : AndroidViewModel(application) {

    private val _bookState = MutableStateFlow<BooksListState>(BooksListState.Idle)
    val bookState = _bookState.asStateFlow()
    private val bookRepo = BooksRepository(application)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBooksByCategory(subject: String) {
        viewModelScope.launch {
            bookRepo.refreshBooks(subject)

            bookRepo.getBooks(subject).collect { books ->
                _bookState.value = BooksListState.Success(books)
            }
        }

    }

    fun resetState () {
        _bookState.value = BooksListState.Idle
    }
}