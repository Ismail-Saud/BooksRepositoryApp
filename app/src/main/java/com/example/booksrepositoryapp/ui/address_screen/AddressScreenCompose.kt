package com.example.booksrepositoryapp.ui.address_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun AddressScreenCompose(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    LazyColumn() {
        item {
            Row() {
                IconButton(
                    onClick = {
                        onBackClick()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
                Text("Address List")
            }
        }
        item {
            // Address Lists
        }
        item {
            OutlinedButton (
                onClick = {
                    onAddClick()
                }
            ) {
                Text("Add Delivery Address")
            }
        }
        item {
            OutlinedButton (
                onClick = {
                    onDeleteClick()
                }
            ) {
                Text("Delete All Addresses")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    BooksRepositoryAppTheme {
        AddressScreenCompose (
            onBackClick = {},
            onAddClick = {},
            onDeleteClick = {}
        )
    }
}