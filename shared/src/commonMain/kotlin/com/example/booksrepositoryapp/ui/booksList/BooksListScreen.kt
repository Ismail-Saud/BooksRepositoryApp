package com.example.booksrepositoryapp.ui.booksList

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.model.Category
import com.example.booksrepositoryapp.ui.bookCategory.imageResource
import com.example.booksrepositoryapp.ui.bookCategory.titleResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import coil3.compose.SubcomposeAsyncImage

@Composable
fun BooksListScreen(
    viewModel: BooksListViewModel,
    onNavigate: (BooksListEffect.NavigateToBookDetails) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.bookState.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val lifecycleOwner = LocalLifecycleOwner.current
    var isResumed by remember {
        mutableStateOf(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, _ ->
            isResumed = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is BooksListEffect.NavigateToBookDetails -> {
                        if (isResumed) {
                            onNavigate(effect)
                        }
                    }
                    BooksListEffect.NavigateBack -> {
                        if (isResumed) {
                            onBackClick()
                        }
                    }
                    is BooksListEffect.ShowError -> {
                        snackbarHostState.showSnackbar(message = effect.message)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
        ) {
            IconButton(
                onClick = { viewModel.onEvent(BooksListEvent.BackClicked) },
                modifier =
                    Modifier
                        .size(48.dp)
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                )
            }
            Text(
                text = viewModel.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    viewModel.onEvent(BooksListEvent.SearchQueryChanged(query.trim()))
                },
                label = {
                    Text("Search")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                    )
                },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = {
                    showFilterSheet = true
                },
            ) {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "Filter",
                )
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp),
        )
        when (val currentState = state) {
            BooksListState.Idle -> {}
            BooksListState.Loading -> {
                BooksGridLoading(
                    modifier = Modifier.weight(1f),
                )
            }
            is BooksListState.Error -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = currentState.message,
                        fontSize = 16.sp,
                    )
                }
            }
            is BooksListState.Success -> {
                if (currentState.books.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No books found",
                            fontSize = 16.sp,
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(4.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = currentState.books,
                            key = { book -> book.id },
                        ) { book ->
                            BookCard(
                                book = book,
                                isClickable = isResumed,
                                onClick = {
                                    if (isResumed) {
                                        viewModel.onEvent(BooksListEvent.BookClicked(book.id))
                                    }
                                },
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
                viewModel.onEvent(BooksListEvent.FilterByPrice(min, max))
                showFilterSheet = false
            },
        )
    }
}

@Composable
fun BookCard(
    book: Book,
    isClickable: Boolean = true,
    onClick: () -> Unit,
) {
    val category = Category(book.category)
    val categoryTitle = stringResource(category.titleResource())
    val categoryImage = category.imageResource()

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable(enabled = isClickable) {
                    onClick()
                },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.LightGray),
                contentAlignment = Alignment.Center,
            ) {
                val imageUrl =
                    if (book.coverId != 0) {
                        "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg"
                    } else {
                        null
                    }

                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Image(
                            painter = painterResource(categoryImage),
                            contentDescription = book.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    },
                )
            }
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFF151515))
                        .padding(8.dp),
            ) {
                Text(
                    text = categoryTitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                )
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                )
                Text(
                    text = book.author,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "$${book.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun BooksGridLoading(modifier: Modifier = Modifier) {
    val transition =
        rememberInfiniteTransition(
            label = "shimmer",
        )
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 1000,
                        easing = LinearEasing,
                    ),
                repeatMode = RepeatMode.Restart,
            ),
        label = "shimmer",
    )
    val shimmerColors =
        listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.6f),
        )
    val shimmerBrush =
        Brush.linearGradient(
            colors = shimmerColors,
            start =
                Offset(
                    translateAnimation - 300f,
                    0f,
                ),
            end =
                Offset(
                    translateAnimation,
                    0f,
                ),
        )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(6) {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(shimmerBrush),
                    )
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.4f)
                                    .height(12.dp)
                                    .background(shimmerBrush),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(18.dp)
                                    .background(shimmerBrush),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(12.dp)
                                    .background(shimmerBrush),
                        )
                    }
                }
            }
        }
    }
}
