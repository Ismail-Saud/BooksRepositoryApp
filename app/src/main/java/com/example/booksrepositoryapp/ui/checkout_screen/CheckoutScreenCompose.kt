package com.example.booksrepositoryapp.ui.checkout_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun CheckoutScreen(
    total: Double,
    selectedAddress: AddressModel?,
    onBackClick: () -> Unit,
    onSelectAddressClick: () -> Unit,
    onPayClick: () -> Unit,
    viewModel: CheckoutViewModel
) {
    var selectedPayment by rememberSaveable {
        mutableStateOf("COD")
    }
    var cardNumber by rememberSaveable {
        mutableStateOf("")
    }
    var cardHolder by rememberSaveable {
        mutableStateOf("")
    }
    var expiry by rememberSaveable {
        mutableStateOf("")
    }
    var cvv by rememberSaveable {
        mutableStateOf("")
    }
    var showCardErrors by rememberSaveable {
        mutableStateOf(false)
    }
    val hasSelectedAddress = selectedAddress != null
    val isCardValid = remember(
        cardNumber,
        cardHolder,
        expiry,
        cvv
    ) {
        viewModel.isValidCardNumber(cardNumber) &&
                viewModel.isValidCardHolderName(cardHolder) &&
                viewModel.isValidExpiryDate(expiry) &&
                viewModel.isValidCVV(cvv)
    }
    val isPayEnabled = total > 0.0 &&
            hasSelectedAddress && (selectedPayment == "COD" || isCardValid)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Checkout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            item {
                Text(
                    text = "Delivery Address",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF151515)
                    )
                ) {
                    Text(
                        text = selectedAddress?.fullAddress
                            ?: "Address not found",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                OutlinedButton(
                    onClick = onSelectAddressClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF555555)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF333333)
                    )
                ) {
                    Text("Select Delivery Address")
                }
            }
            item {
                Text(
                    text = "Payment Method",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPayment == "CARD",
                            onClick = {
                                selectedPayment = "CARD"
                            }
                        )
                        Text(
                            text = "Credit / Debit Card",
                            fontSize = 18.sp
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPayment == "COD",
                            onClick = {
                                selectedPayment = "COD"
                                showCardErrors = false
                            }
                        )
                        Text(
                            text = "Cash on Delivery",
                            fontSize = 18.sp
                        )
                    }
                }
            }
            if (selectedPayment == "CARD") {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = {
                                if (
                                    it.length <= 16 &&
                                    it.all(Char::isDigit)
                                ) {
                                    cardNumber = it
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            label = {
                                Text("Card Number")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true,
                            isError = showCardErrors &&
                                    !viewModel.isValidCardNumber(cardNumber)
                        )
                        if (
                            showCardErrors &&
                            !viewModel.isValidCardNumber(cardNumber)
                        ) {
                            Text(
                                text = "Invalid card number",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 4.dp
                                )
                            )
                        }
                        OutlinedTextField(
                            value = cardHolder,
                            onValueChange = {
                                cardHolder = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            label = {
                                Text("Card Holder Name")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            singleLine = true,
                            isError = showCardErrors &&
                                    !viewModel.isValidCardHolderName(cardHolder)
                        )
                        if (
                            showCardErrors &&
                            !viewModel.isValidCardHolderName(cardHolder)
                        ) {
                            Text(
                                text = "Invalid card holder name",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 4.dp
                                )
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 16.dp,
                                    bottom = 16.dp
                                )
                        ) {
                            OutlinedTextField(
                                value = expiry,
                                onValueChange = {
                                    if (
                                        it.length <= 5 &&
                                        it.all {
                                                char ->
                                            char.isDigit() ||
                                                    char == '/'
                                        }
                                    ) {
                                        expiry = it
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                label = {
                                    Text("MM/YY")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                isError = showCardErrors &&
                                        !viewModel.isValidExpiryDate(expiry)
                            )
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = {
                                    if (
                                        it.length <= 3 &&
                                        it.all(Char::isDigit)
                                    ) {
                                        cvv = it
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                label = {
                                    Text("CVV")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.NumberPassword
                                ),
                                singleLine = true,
                                isError = showCardErrors &&
                                        !viewModel.isValidCVV(cvv)
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (selectedPayment == "CARD") {
                    if (!isCardValid) {
                        showCardErrors = true
                        return@Button
                    }
                }
                onPayClick()
            },
            enabled = isPayEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = "Pay $%.2f".format(total),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    BooksRepositoryAppTheme {
//        CheckoutScreen (
//            total = 0.0,
//            onBackClick = {},
//            onSelectAddressClick = {},
//            onPayClick = {},
//
//        )
    }
}