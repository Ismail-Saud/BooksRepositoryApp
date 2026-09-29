package com.example.booksrepositoryapp.ui.bookCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.domain.model.Category
import com.example.booksrepositoryapp.domain.model.categories
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BooksCategoryViewModel : ViewModel() {
    private var activeSearch = ""
    private var allCategories: List<Category> = emptyList()
    private val _categoryState = MutableStateFlow<BooksCategoryState>(BooksCategoryState.Idle)
    val categoryState: StateFlow<BooksCategoryState> = _categoryState.asStateFlow()
    private val _effect = Channel<BooksCategoryEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: BooksCategoryEvent) {
        when (event) {
            is BooksCategoryEvent.SearchQueryChanged -> searchCategories(event.query)
            is BooksCategoryEvent.CategoryClicked -> {
                viewModelScope.launch {
                    _effect.send(BooksCategoryEffect.NavigateToBooksList(event.apiValue, event.title))
                }
            }
            BooksCategoryEvent.RefreshCategories -> setCategories(categories)
        }
    }

    fun setCategories(categories: List<Category>) {
        allCategories = categories
        _categoryState.value = BooksCategoryState.Success(allCategories)
    }

    fun searchCategories(query: String) {
        activeSearch = query
        viewModelScope.launch {
            val result = if (activeSearch.isEmpty()) {
                allCategories
            } else {
                allCategories.filter { category ->
                    category.title.contains(activeSearch, ignoreCase = true) ||
                            category.apiValue.contains(activeSearch, ignoreCase = true)
                }
            }
            _categoryState.value = BooksCategoryState.Success(result)
        }
    }

    init {
        setCategories(categories)
    }
}
