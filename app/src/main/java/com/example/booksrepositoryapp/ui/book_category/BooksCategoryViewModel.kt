package com.example.booksrepositoryapp.ui.book_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.api.models.Category
import kotlinx.coroutines.launch

class BooksCategoryViewModel : ViewModel() {
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
                searchResult.filter { it ->
                    it.title.contains(activeSearch, ignoreCase = true)
                }
            }
            _categoryState.value = BooksCategoryState.Success(result)
        }
    }
}