package com.example.booksrepositoryapp.ui.bookCategory

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.data.source.remote.retrofit.dto.Category
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun BookCategoryScreen(
    viewModel: BooksCategoryViewModel,
    onNavigate: (BooksCategoryEffect.NavigateToBooksList) -> Unit
) {
    val state by viewModel.categoryState.observeAsState(BooksCategoryState.Idle)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BooksCategoryEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is BooksCategoryEffect.NavigateToBooksList -> {
                    onNavigate(effect)
                }
            }
        }
    }
    when (val currentState = state) {
        is BooksCategoryState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BooksCategoryState.Success -> {
            BookCategoryContent(
                categories = currentState.categories,
                onSearchQueryChanged = { viewModel.onEvent(BooksCategoryEvent.SearchQueryChanged(it)) },
                onCategoryClicked = { apiValue, title ->
                    viewModel.onEvent(BooksCategoryEvent.CategoryClicked(apiValue, title))
                }
            )
        }

        is BooksCategoryState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = currentState.message, color = Color.Red)
                    Button(onClick = { viewModel.onEvent(BooksCategoryEvent.RefreshCategories) }) {
                        Text("Retry")
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun BookCategoryContent(
    categories: List<Category>,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryClicked: (String, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearchQueryChanged(it)
            },
            label = { Text("Search Category") },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClicked(category.apiValue, category.title) }
                )
            }
        }
    }
}
@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            Image(
                painter = painterResource(category.imgSrc),
                contentDescription = category.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.7f
            )

            Text(
                text = category.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BooksCategoryScreenComposePreview() {
    BooksRepositoryAppTheme {
        BookCategoryContent(
            categories = emptyList(),
            onSearchQueryChanged = {},
            onCategoryClicked = { _, _ -> }
        )
    }
}
