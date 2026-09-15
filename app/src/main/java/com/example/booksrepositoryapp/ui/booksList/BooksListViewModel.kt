package com.example.booksrepositoryapp.ui.booksList

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.BooksRepositoryImpl
import com.example.booksrepositoryapp.data.util.refreshResult.RefreshResult
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.usecase.GetBooksUseCase
import com.example.booksrepositoryapp.domain.usecase.RefreshBooksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class BooksListViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val _bookState = MutableStateFlow<BooksListState>(BooksListState.Idle)
    val bookState = _bookState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    private var allBooks: List<Book> = emptyList()
    private var minPrice = 0
    private var maxPrice = Int.MAX_VALUE

    private var fetchBooksJob: Job? = null

    private val bookRepo = BooksRepositoryImpl(application)
    private val getBooksUseCase = GetBooksUseCase(bookRepo)
    private val refreshBooksUseCase = RefreshBooksUseCase(bookRepo)

    private val apiValue: String = savedStateHandle["apiValue"] ?: ""
    val title: String = savedStateHandle["title"] ?: "Unknown"

    private val _effect = Channel<BooksListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: BooksListEvent) {
        when (event) {
            is BooksListEvent.SearchQueryChanged -> searchBooks(event.query)
            is BooksListEvent.BookClicked -> {
                _effect.trySend(BooksListEffect.NavigateToBookDetails(event.bookId))
            }
            is BooksListEvent.FilterByPrice -> filterByPrice(event.minPrice, event.maxPrice)
            BooksListEvent.RefreshBooks -> getBooksByCategory(apiValue)
            BooksListEvent.BackClicked -> {
                _effect.trySend(BooksListEffect.NavigateBack)
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBooksByCategory(subject: String) {
        fetchBooksJob?.cancel()
        fetchBooksJob = viewModelScope.launch {
            _bookState.value = BooksListState.Loading
            val result = withContext(Dispatchers.IO) {
                refreshBooksUseCase(subject)
            }

            when (result) {
                is RefreshResult.Offline -> _bookState.value = BooksListState.Offline
                is RefreshResult.Error -> _bookState.value = BooksListState.Error(result.message)
                else -> { /* Continue to collection */ }
            }

            getBooksUseCase(subject).collect { books ->
                allBooks = books
                if (books.isNotEmpty()) {
                    applyFilters()
                } else if (result is RefreshResult.Success) {
                    _bookState.value = BooksListState.Success(emptyList())
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
            val matchesPrice = (book.price?.toInt() ?: 0) in minPrice..maxPrice
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
        getBooksByCategory(apiValue)
    }
}
