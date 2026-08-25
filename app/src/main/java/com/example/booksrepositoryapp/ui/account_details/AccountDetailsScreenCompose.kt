package com.example.booksrepositoryapp.ui.account_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable 
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun AccountDetailsScreen(
    viewModel: AccountDetailsViewModel,
    onLogoutClick: () -> Unit
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val selectedAddress by viewModel.selectedAddress.collectAsStateWithLifecycle(
        initialValue = null
    )
    LaunchedEffect(Unit) {
        viewModel.getUser()
    }
    val user = when (val state = userState) {
        is AccountDetailsState.Success -> state.user
        else -> null
    }
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                text = "Account",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        item {
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        item {
            AccountInfoCard(
                label = "Name:",
                value = user?.username ?: "Name not found",
                modifier = Modifier.padding(top = 32.dp)
            )

            AccountInfoCard(
                label = "E-mail:",
                value = user?.email ?: "Email not found",
                modifier = Modifier.padding(top = 16.dp)
            )

            AccountInfoCard(
                label = "Address:",
                value = selectedAddress?.fullAddress ?: "Address not found",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        item {
            OutlinedButton(
                onClick = {
                    onLogoutClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        top = 24.dp
                    )
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Log out",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AccountInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 55.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            Text(
                text = label,
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.CenterVertically),
                color = Color.Black
            )

            Text(
                text = value,
                modifier = Modifier.weight(1f),
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddToCartPreview() {
    BooksRepositoryAppTheme {
        AccountDetailsScreen(
            viewModel = viewModel(),
            onLogoutClick = {}
        )
    }
}