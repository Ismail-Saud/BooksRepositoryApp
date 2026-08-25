package com.example.booksrepositoryapp.ui.books_screen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun BooksListScreen(
    apiValue: String,
    title: String,
    viewModel: BooksListViewModel,
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit
) {
    val state by viewModel.bookState.collectAsStateWithLifecycle()
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    var showFilterSheet by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(apiValue) {
        viewModel.getBooksByCategory(apiValue)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    viewModel.searchBooks(
                        query.trim()
                    )
                },
                label = {
                    Text("Search")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    showFilterSheet = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "Filter"
                )
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        when (val currentState = state) {
            BooksListState.Idle -> {}
            BooksListState.Loading -> {
                BooksGridLoading(
                    modifier = Modifier.weight(1f)
                )
            }
            is BooksListState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        fontSize = 16.sp
                    )
                }
            }
            is BooksListState.Success -> {
                if (currentState.books.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No books found",
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = currentState.books,
                            key = { book -> book.workId }
                        ) { book ->
                            BookCard(
                                book = book,
                                onClick = {
                                    onBookClick(book.workId)
                                }
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
    if (showFilterSheet) {
        PriceFilterBottomSheet(
            onDismiss = {
                showFilterSheet = false
            },
            onApply = { min, max ->
                viewModel.filterByPrice(
                    min,
                    max
                )
                showFilterSheet = false
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookCard(
    book: BookDetailsModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (book.coverId != 0) {
                    GlideSubcomposition(
                        model = "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (state) {
                            RequestState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            is RequestState.Success -> {
                                GlideImage(
                                    model = "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg",
                                    contentDescription = book.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            RequestState.Failure -> {
                                Image(
                                    painter = painterResource(R.drawable.book_cover_img),
                                    contentDescription = book.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(R.drawable.book_cover_img),
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF151515))
                    .padding(8.dp)
            ) {
                Text(
                    text = book.category.replaceFirstChar { it.uppercaseChar() },
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2
                )
                Text(
                    text = book.author,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${book.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun BooksGridLoading(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(6) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .shimmerEffect()
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(12.dp)
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(18.dp)
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(12.dp)
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
    }
}

fun Modifier.shimmerEffect(): Modifier {
    return composed {
        val transition = rememberInfiniteTransition(
            label = "shimmer"
        )
        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer"
        )
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.6f)
        )
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation - 300f, 0f),
            end = Offset(translateAnimation, 0f)
        )
        background(brush)
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview(showBackground = true)
@Composable
fun BooksListScreenComposePreview() {
    BooksRepositoryAppTheme {
        BooksListScreen(
            apiValue = "classic",
            title = "Classic",
            viewModel = viewModel(),
            onBackClick = {},
            onBookClick = {}
        )
    }
}

