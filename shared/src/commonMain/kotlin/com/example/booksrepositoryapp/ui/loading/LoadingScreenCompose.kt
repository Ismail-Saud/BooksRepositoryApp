package com.example.booksrepositoryapp.ui.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingScreenCompose(showDialog: Boolean) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {},
        ) {
            Card(
                modifier = Modifier.width(240.dp),
                shape = RoundedCornerShape(16.dp),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 4.dp,
                    ),
                colors =
                    CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                    )

                    Text(
                        text = "Processing Order...",
                        modifier = Modifier.padding(top = 12.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}
