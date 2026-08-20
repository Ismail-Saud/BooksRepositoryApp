package com.example.booksrepositoryapp.ui.books_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceFilterBottomSheet(
    onApply: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var priceRange by remember {
        mutableStateOf(15f..36f)
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price Filter",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            Text(
                text = "Price: $${priceRange.start.toInt()} - $${priceRange.endInclusive.toInt()}",
                fontSize = 16.sp
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            RangeSlider(
                value = priceRange,
                onValueChange = {
                    priceRange = it
                },
                valueRange = 15f..36f,
                steps = 20
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            Button(
                onClick = {
                    onApply(
                        priceRange.start.toInt(),
                        priceRange.endInclusive.toInt()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filter")
            }
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            OutlinedButton(
                onClick = {
                    priceRange = 15f..36f
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Filter")
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}