package com.example.booksrepositoryapp.ui.conformation_bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartScreen
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheetCompose(
    title: String,
    message: String,
    positiveButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 24.dp
                )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                fontSize = 15.sp,
                color = Color(0xFF555555),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = Color(0xFF111111)
                    )
                }
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF111111),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = positiveButtonText
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationBottomSheetComposePreview() {
    BooksRepositoryAppTheme {
        ConfirmationBottomSheetCompose(
            title = "",
            message = "",
            positiveButtonText = "",
            onConfirm = {},
            onDismiss = {}
        )
    }
}