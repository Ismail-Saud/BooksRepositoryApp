package com.example.booksrepositoryapp.ui.addToCart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestCoordinator
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.ui.conformationBottomSheet.ConfirmationBottomSheetCompose
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun AddToCartScreen(
    viewModel: AddToCartViewModel,
    onNavigate: (AddToCartEffect.NavigateToCheckout) -> Unit,
    shippingFee: Double
) {
    val state by viewModel.addToCartState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                is AddToCartEffect.NavigateToCheckout -> onNavigate(effect)
                is AddToCartEffect.ShowToast -> {}
            }
        }
    }

    when(val currentState = state) {
        AddToCartState.Idle -> {}
        AddToCartState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF111111))
            }
        }

        is AddToCartState.Error -> {
            Toast.makeText(context, currentState.message, Toast.LENGTH_SHORT).show()
        }

        is AddToCartState.Success -> {
            val carts = currentState.cart
            val subTotal = carts.sumOf { it.price * it.quantity }
            val shipping = if (carts.isEmpty()) 0.0 else shippingFee
            val total = subTotal + shipping
            var showConfirmation by remember {
                mutableStateOf(false)
            }
            var selectedCart by remember {
                mutableStateOf<Cart?>(null)
            }
            if (showConfirmation && selectedCart != null) {
                ConfirmationBottomSheetCompose(
                    title = "Remove Item",
                    message = "Remove this item from your cart?",
                    positiveButtonText = "Remove",
                    onConfirm = {
                        selectedCart?.let { cartItem ->
                            viewModel.onEvent(AddToCartEvent.RemoveItem(cartItem))
                        }
                        selectedCart = null
                        showConfirmation = false
                    },
                    onDismiss = {
                        selectedCart = null
                        showConfirmation = false
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Cart",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val isLandscape = maxWidth > maxHeight
                    if (isLandscape) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 14.dp)
                        ) {
                            items(items = carts, key = { it.bookId }) { cartItem ->
                                CartItemView(
                                    cart = cartItem,
                                    onRemoveClick = {
                                        selectedCart = cartItem
                                        showConfirmation = true
                                    },
                                    onIncreaseClick = {
                                        viewModel.onEvent(AddToCartEvent.IncreaseQuantity(cartItem))
                                    },
                                    onDecreaseClick = {
                                        if (cartItem.quantity == 1) {
                                            selectedCart = cartItem
                                            showConfirmation = true
                                        } else {
                                            viewModel.onEvent(AddToCartEvent.DecreaseQuantity(cartItem))
                                        }
                                    }
                                )
                            }
                            item {
                                OrderSummary(subTotal, shipping, total)
                            }
                            item {
                                CheckoutButton(carts.isNotEmpty()) {
                                    viewModel.onEvent(AddToCartEvent.ProceedToCheckout(total))
                                }
                            }
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                            ) {
                                items(items = carts, key = { it.bookId }) { cartItem ->
                                    CartItemView(
                                        cart = cartItem,
                                        onRemoveClick = {
                                            selectedCart = cartItem
                                            showConfirmation = true
                                        },
                                        onIncreaseClick = {
                                            viewModel.onEvent(AddToCartEvent.IncreaseQuantity(cartItem))
                                        },
                                        onDecreaseClick = {
                                            if (cartItem.quantity == 1) {
                                                selectedCart = cartItem
                                                showConfirmation = true
                                            } else {
                                                viewModel.onEvent(AddToCartEvent.DecreaseQuantity(cartItem))
                                            }
                                        }
                                    )
                                }
                            }
                            OrderSummary(subTotal, shipping, total, Modifier.padding(10.dp))
                            CheckoutButton(carts.isNotEmpty(), Modifier.padding(10.dp)) {
                                viewModel.onEvent(AddToCartEvent.ProceedToCheckout(total))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSummary(subTotal: Double, shipping: Double, total: Double, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Order Summary",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        SummaryRow("Subtotal", subTotal)
        Spacer(modifier = Modifier.height(10.dp))
        SummaryRow("Shipping", shipping)
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Total", fontSize = 18.sp, color = Color(0xFF222222))
            Text(text = "$%.2f".format(total), fontSize = 18.sp, color = Color(0xFF222222))
        }
    }
}

@Composable
fun SummaryRow(label: String, value: Double) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF555555))
        Text(text = "$%.2f".format(value), fontSize = 14.sp, color = Color(0xFF222222))
    }
}

@Composable
fun CheckoutButton(enabled: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF111111),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF696969),
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Proceed to Checkout", fontSize = 14.sp)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartItemView(
    cart: Cart,
    onRemoveClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(bottom = 14.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(84.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFBDBDBD)),
                contentAlignment = Alignment.Center
            ) {
                if (cart.coverId != 0) {
                    SubcomposeAsyncImage(
                        model = "https://covers.openlibrary.org/b/id/${cart.coverId}-L.jpg",
                        contentDescription = cart.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        },
                        error = {
                            Image(
                                painter = painterResource(R.drawable.book_cover_img),
                                contentDescription = cart.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.book_cover_img),
                        contentDescription = cart.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 12.dp, end = 32.dp)) {
                    Text(text = cart.category, color = Color(0xFF888888), fontSize = 9.sp)
                    Text(
                        text = cart.title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = cart.author,
                        color = Color(0xFFAAAAAA),
                        fontSize = 8.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuantityButton(Icons.Default.Remove, onDecreaseClick)
                    Text(
                        text = cart.quantity.toString(),
                        modifier = Modifier.width(24.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    QuantityButton(Icons.Default.Add, onIncreaseClick)
                }
            }
        }
    }
}

@Composable
fun QuantityButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF111111), modifier = Modifier.size(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AddToCartPreview() {
    BooksRepositoryAppTheme {
        AddToCartScreen(
            viewModel = viewModel(),
            onNavigate = {},
            shippingFee = 5.0
        )
    }
}
