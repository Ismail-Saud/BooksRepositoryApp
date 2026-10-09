package com.example.booksrepositoryapp.ui.bookDetails

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.booksrepositoryapp.domain.model.Book
import com.example.booksrepositoryapp.domain.model.Category
import com.example.booksrepositoryapp.ui.bookCategory.imageResource
import com.example.booksrepositoryapp.ui.bookCategory.titleResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookDetailsScreen(
    viewModel: BookDetailsViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.bookDetailState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    BookDetailsEffect.NavigateBack -> {
                        onBackClick()
                    }

                    is BookDetailsEffect.ShowMessage -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = effect.message,
                            )
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val currentState = state) {
            BookDetailsState.Idle -> Unit
            BookDetailsState.Loading -> {
                BookDetailsShimmer()
            }
            is BookDetailsState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = currentState.message,
                    )
                }
            }
            is BookDetailsState.Success -> {
                currentState.books?.let { book ->
                    BookDetailsContent(
                        book = book,
                        isAddingToCart = currentState.isAddingToCart,
                        onBackClick = {
                            onBackClick()
                        },
                        onAddToCartClick = {
                            viewModel.onEvent(
                                BookDetailsEvent.AddToCartClicked,
                            )
                        },
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
fun BookDetailsContent(
    book: Book,
    isAddingToCart: Boolean = false,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
) {
    val category = Category(book.category)
    val categoryTitle = stringResource(category.titleResource())
    val categoryImage = category.imageResource()

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
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
            start = Offset(translateAnimation - 300f, 0f),
            end = Offset(translateAnimation, 0f),
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
        ) {
            IconButton(
                onClick = onBackClick,
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
                text = categoryTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        Text(
            text = book.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(12.dp),
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .width(130.dp)
                        .height(190.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (book.coverId != 0) {
                    SubcomposeAsyncImage(
                        model = "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg",
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize().background(shimmerBrush),
                            )
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
                } else {
                    Image(
                        painter = painterResource(categoryImage),
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Column(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
            ) {
                Text(
                    text = "Author : ${book.author}",
                    fontSize = 18.sp,
                    color = Color(0xFF212121),
                )
                Text(
                    text = "Category : $categoryTitle",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    text = "Rating : ${book.rating}/5",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Pricing:",
                        fontSize = 18.sp,
                    )
                    Text(
                        text = " $${book.price}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Button(
                    onClick = {
                        onAddToCartClick()
                    },
                    enabled = !isAddingToCart,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF111111),
                            disabledContainerColor = Color(0xFF555555),
                        ),
                ) {
                    if (isAddingToCart) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = "Add to Cart",
                            fontSize = 18.sp,
                            color = Color.White,
                        )
                    }
                }
            }
        }
        Text(
            text = "Description:",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier =
                Modifier
                    .padding(
                        top = 24.dp,
                        start = 12.dp,
                        end = 12.dp,
                    ),
        )

        Text(
            text = book.description ?: "",
            fontSize = 18.sp,
            color = Color(0xFF424242),
            lineHeight = 22.sp,
            modifier =
                Modifier
                    .padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp,
                    ),
        )
    }
}

@Composable
fun BookDetailsShimmer() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
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
            start = Offset(translateAnimation - 300f, 0f),
            end = Offset(translateAnimation, 0f),
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .background(shimmerBrush, shape = RoundedCornerShape(20.dp)),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier =
                    Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth(0.8f)
                    .height(32.dp)
                    .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier =
                    Modifier
                        .width(130.dp)
                        .height(190.dp)
                        .background(shimmerBrush, shape = RoundedCornerShape(8.dp)),
            )

            Column(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.9f)
                            .height(20.dp)
                            .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.7f)
                            .height(20.dp)
                            .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.5f)
                            .height(20.dp)
                            .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.6f)
                            .height(28.dp)
                            .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(shimmerBrush, shape = RoundedCornerShape(6.dp)),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier =
                Modifier
                    .width(140.dp)
                    .height(24.dp)
                    .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(0.95f)
                    .height(16.dp)
                    .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(0.9f)
                    .height(16.dp)
                    .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .background(shimmerBrush, shape = RoundedCornerShape(4.dp)),
        )
    }
}
