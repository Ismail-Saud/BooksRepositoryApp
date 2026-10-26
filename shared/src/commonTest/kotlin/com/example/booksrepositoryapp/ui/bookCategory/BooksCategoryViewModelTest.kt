package com.example.booksrepositoryapp.ui.bookCategory

import com.example.booksrepositoryapp.domain.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BooksCategoryViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchMatchesCategoryNamesWrittenWithSpacesOrHyphens() = runTest(dispatcher) {
        val viewModel = BooksCategoryViewModel()
        viewModel.setCategories(listOf(Category("young_adult"), Category("fantasy")))

        viewModel.searchCategories("young adult")
        advanceUntilIdle()

        assertEquals(
            BooksCategoryState.Success(listOf(Category("young_adult"))),
            viewModel.categoryState.value,
        )
    }

    @Test
    fun categoryClickSendsNavigationWithTheSelectedValues() = runTest(dispatcher) {
        val viewModel = BooksCategoryViewModel()

        viewModel.onEvent(BooksCategoryEvent.CategoryClicked("fantasy", "Fantasy"))
        advanceUntilIdle()

        assertEquals(
            BooksCategoryEffect.NavigateToBooksList("fantasy", "Fantasy"),
            viewModel.effect.first(),
        )
    }
}
