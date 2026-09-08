package com.example.booksrepositoryapp.ui.bookCategory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.retrofit.dto.Category
import com.example.booksrepositoryapp.data.source.remote.retrofit.dto.categories
import kotlinx.coroutines.launch

class BooksCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private var activeSearch = ""
    private var allCategories: List<Category> = emptyList()
    private val _categoryState = MutableLiveData<BooksCategoryState>(BooksCategoryState.Idle)
    val categoryState: LiveData<BooksCategoryState> = _categoryState

    fun setCategories(categories: List<Category>) {
        allCategories = categories
        _categoryState.value = BooksCategoryState.Success(allCategories)
    }

    fun searchTodos (query: String) {
        activeSearch = query
        viewModelScope.launch {
            val searchResult = allCategories
            val result = if (activeSearch.isEmpty()) {
                searchResult
            } else {
                searchResult.filter { category ->
                    category.apiValue.contains(activeSearch, ignoreCase = true)
                }
            }
            _categoryState.value = BooksCategoryState.Success(result)
        }
    }

    fun resetState () {
        _categoryState.value = BooksCategoryState.Idle
    }

    init {
        setCategories(categories)
    }
}