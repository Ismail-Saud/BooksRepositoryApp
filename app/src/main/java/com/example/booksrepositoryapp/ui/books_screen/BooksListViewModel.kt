package com.example.booksrepositoryapp.ui.books_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksListViewModel(application: Application) : AndroidViewModel(application) {

    private val _bookState = MutableStateFlow<BooksListState>(BooksListState.Idle)
    val bookState = _bookState.asStateFlow()
    private val bookRepo = BooksRepository()

    fun getBooksByCategory(subject: String) {
        viewModelScope.launch {
            val response = bookRepo.getBooksByCategory(subject)
            _bookState.value = BooksListState.Success(response.works)
        }
    }

    fun resetState () {
        _bookState.value = BooksListState.Idle
    }
}