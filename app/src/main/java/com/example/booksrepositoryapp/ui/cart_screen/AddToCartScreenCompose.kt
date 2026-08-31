package com.example.booksrepositoryapp.ui.cart_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState

@Composable
fun AddToCartScreen(
    cartItems: List<CartItem>,
    onCheckoutClick: (Double) -> Unit,
    onRemoveClick: (CartItem) -> Unit,
    onIncreaseClick: (CartItem) -> Unit,
    onDecreaseClick: (CartItem) -> Unit
) {
    val subTotal = cartItems.sumOf {
        it.price * it.quantity
    }
    val shipping = if (cartItems.isEmpty()) { 0.0 } else { 10.0 }
    val total = subTotal + shipping
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 14.dp
                    )
                ) {
                    items(
                        items = cartItems,
                        key = { it.cartId }
                    ) { cartItem ->
                        CartItem(
                            cartItem = cartItem,
                            onRemoveClick = {
                                onRemoveClick(cartItem)
                            },
                            onIncreaseClick = {
                                onIncreaseClick(cartItem)
                            },
                            onDecreaseClick = {
                                onDecreaseClick(cartItem)
                            }
                        )
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = "Order Summary",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF222222),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Subtotal",
                                    fontSize = 14.sp,
                                    color = Color(0xFF555555)
                                )

                                Text(
                                    text = "$%.2f".format(subTotal),
                                    fontSize = 14.sp,
                                    color = Color(0xFF222222)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Shipping",
                                    fontSize = 14.sp,
                                    color = Color(0xFF555555)
                                )

                                Text(
                                    text = "$%.2f".format(shipping),
                                    fontSize = 14.sp,
                                    color = Color(0xFF222222)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            HorizontalDivider(
                                color = Color(0xFF444444),
                                thickness = 1.dp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total",
                                    fontSize = 18.sp,
                                    color = Color(0xFF222222)
                                )

                                Text(
                                    text = "$%.2f".format(total),
                                    fontSize = 18.sp,
                                    color = Color(0xFF222222)
                                )
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                onCheckoutClick(total)
                            },
                            enabled = cartItems.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .height(52.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF111111),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF696969),
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Proceed to Checkout",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    ) {
                        items(
                            items = cartItems,
                            key = { it.cartId }
                        ) { cartItem ->
                            CartItem(
                                cartItem = cartItem,
                                onRemoveClick = {
                                    onRemoveClick(cartItem)
                                },
                                onIncreaseClick = {
                                    onIncreaseClick(cartItem)
                                },
                                onDecreaseClick = {
                                    onDecreaseClick(cartItem)
                                }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    ) {
                        Text(
                                text = "Order Summary",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF222222),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                            Text(
                                    text = "Subtotal",
                                    fontSize = 14.sp,
                                    color = Color(0xFF555555)
                                )
                            Text(
                                    text = "$%.2f".format(subTotal),
                                    fontSize = 14.sp,
                                    color = Color(0xFF222222)
                                )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                            Text(
                                    text = "Shipping",
                                    fontSize = 14.sp,
                                    color = Color(0xFF555555)
                                )
                            Text(
                                    text = "$%.2f".format(shipping),
                                    fontSize = 14.sp,
                                    color = Color(0xFF222222)
                                )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                                color = Color(0xFF444444),
                                thickness = 1.dp
                            )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                            Text(
                                text = "Total",
                                fontSize = 18.sp,
                                color = Color(0xFF222222)
                            )
                            Text(
                                text = "$%.2f".format(total),
                                fontSize = 18.sp,
                                color = Color(0xFF222222)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            onCheckoutClick(total)
                        },
                        enabled = cartItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF111111),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF111111),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Proceed to Checkout",
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartItem(
    cartItem: CartItem,
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111111)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .width(84.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFBDBDBD)),
                contentAlignment = Alignment.Center
            ) {
                if (cartItem.coverId != 0) {
                    GlideSubcomposition(
                        model = "https://covers.openlibrary.org/b/id/${cartItem.coverId}-L.jpg",
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
                                    model = "https://covers.openlibrary.org/b/id/${cartItem.coverId}-L.jpg",
                                    contentDescription = cartItem.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            RequestState.Failure -> {
                                Image(
                                    painter = painterResource(R.drawable.book_cover_img),
                                    contentDescription = cartItem.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(R.drawable.book_cover_img),
                        contentDescription = cartItem.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            top = 12.dp,
                            end = 32.dp
                        )
                ) {
                    Text(
                        text = cartItem.category,
                        color = Color(0xFF888888),
                        fontSize = 9.sp
                    )
                    Text(
                        text = cartItem.title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = cartItem.author,
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
                        .padding(end = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color.White
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = 8.dp,
                            bottom = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                onDecreaseClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Text(
                        text = cartItem.quantity.toString(),
                        modifier = Modifier.width(24.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                onIncreaseClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddToCartPreview() {
    BooksRepositoryAppTheme {
        AddToCartScreen (
            cartItems = listOf<CartItem>(),
            onCheckoutClick = {},
            onRemoveClick = {},
            onDecreaseClick = {},
            onIncreaseClick = {}
        )
    }
}