package com.example.booksrepositoryapp.ui.cart_screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun AddToCartScreen(
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(48.dp)
        ) {

            IconButton(
                onClick = {
                    onBackClick()
                },
                modifier = Modifier.Companion.align(Alignment.Companion.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Companion.Black
                )
            }

            Text(
                text = "Cart",
                fontSize = 16.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF111111),
                modifier = Modifier.Companion.align(Alignment.Companion.Center)
            )
        }
        LazyColumn(
            modifier = Modifier.Companion
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        ) {

        }
        Column(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Order Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.Companion.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Subtotal",
                    fontSize = 14.sp,
                    color = Color(0xFF555555)
                )

                Text(
                    text = "$50.00",
                    fontSize = 14.sp,
                    color = Color(0xFF222222)
                )
            }

            Spacer(modifier = Modifier.Companion.height(10.dp))
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shipping",
                    fontSize = 14.sp,
                    color = Color(0xFF555555)
                )

                Text(
                    text = "$10.00",
                    fontSize = 14.sp,
                    color = Color(0xFF222222)
                )
            }

            Spacer(modifier = Modifier.Companion.height(12.dp))

            HorizontalDivider(
                color = Color(0xFF444444),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.Companion.height(10.dp))

            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    fontSize = 18.sp,
                    color = Color(0xFF222222)
                )

                Text(
                    text = "$60.00",
                    fontSize = 18.sp,
                    color = Color(0xFF222222)
                )
            }
        }

        Button(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 0.dp, bottom = 0.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF111111),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Proceed to Checkout",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.Companion.height(14.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AddToCartPreview() {
    BooksRepositoryAppTheme {
        AddToCartScreen(
            onBackClick = {},
            onCheckoutClick = {}
        )
    }
}