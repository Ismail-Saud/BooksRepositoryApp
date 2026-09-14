package com.example.booksrepositoryapp.ui.checkout

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun CheckoutScreen(
    total: Double,
    onNavigate: (CheckoutEffect) -> Unit,
    viewModel: CheckoutViewModel,
    isCheckoutEnabled: Boolean = true
) {
    val checkoutState by viewModel.checkoutState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(isCheckoutEnabled) {
        if (!isCheckoutEnabled) {
            Toast.makeText(context, "Checkout is currently under maintenance", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CheckoutEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                else -> onNavigate(effect)
            }
        }
    }

    when (val state = checkoutState) {
        CheckoutState.Idle -> {}
        is CheckoutState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
        is CheckoutState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = Color.Red)
            }
        }
        is CheckoutState.Success -> {
            val selectedAddress = state.address

            var selectedPayment by rememberSaveable {
                mutableStateOf("COD")
            }
            var cardNumber by rememberSaveable {
                mutableStateOf("")
            }
            var cardHolder by rememberSaveable {
                mutableStateOf("")
            }
            var expiry by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
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
                        viewModel.isValidExpiryDate(expiry.text) &&
                        viewModel.isValidCVV(cvv)
            }
            val isPayEnabled = total > 0.0 && hasSelectedAddress && (selectedPayment == "COD" || isCardValid) && isCheckoutEnabled

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(CheckoutEvent.BackClick)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    if (!isCheckoutEnabled) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFEBEE)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                border = BorderStroke(1.dp, Color.Red)
                            ) {
                                Text(
                                    text = "Checkout is currently disabled for maintenance.",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
                                .padding(top = 16.dp, bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF151515)
                            )
                        ) {
                            Text(
                                text = selectedAddress?.fullAddress ?: "Address not found",
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
                            onClick = {
                                viewModel.onEvent(CheckoutEvent.SelectedAddress)
                            },
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
                                    isError = showCardErrors && !viewModel.isValidCardNumber(cardNumber)
                                )
                                if (
                                    showCardErrors && !viewModel.isValidCardNumber(cardNumber)
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
                                    isError = showCardErrors && !viewModel.isValidCardHolderName(cardHolder)
                                )
                                if (
                                    showCardErrors && !viewModel.isValidCardHolderName(cardHolder)
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
                                        onValueChange = { input ->
                                            val digits = input.text.filter { it.isDigit() }
                                            if (digits.length <= 4) {
                                                val formatted = when {
                                                    digits.length <= 2 -> digits
                                                    else -> "${digits.substring(0, 2)}/${digits.substring(2)}"
                                                }
                                                expiry = TextFieldValue(
                                                    text = formatted,
                                                    selection = TextRange(formatted.length)
                                                )
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
                                        isError = showCardErrors && !viewModel.isValidExpiryDate(expiry.text)
                                    )
                                    OutlinedTextField(
                                        value = cvv,
                                        onValueChange = {
                                            if (it.length <= 3 && it.all(Char::isDigit)) {
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
                                            keyboardType = KeyboardType.NumberPassword
                                        ),
                                        singleLine = true,
                                        isError = showCardErrors && !viewModel.isValidCVV(cvv)
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
                        viewModel.onEvent(CheckoutEvent.PayClicked(selectedPayment, total))
                    },
                    enabled = isPayEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
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
                        text = if (isCheckoutEnabled) "Pay $%.2f".format(total) else "Checkout Disabled",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    BooksRepositoryAppTheme {
        CheckoutScreen(
            total = 2500.0,
            viewModel = viewModel(),
            onNavigate = {}
        )
    }
}
