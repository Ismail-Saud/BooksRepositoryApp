package com.example.booksrepositoryapp.ui.auth.getstarted

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun GetStartedScreen(
    onBackClick: () -> Unit,
    onGetStartedClick: (String, String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    getStartedState: GetStartedState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val isLoading = getStartedState is GetStartedState.Loading
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF111111)
                )
            }
            Text(
                text = "Get Started",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111),
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = "Please fill your details to login.",
            fontSize = 12.sp,
            color = Color(0xFF333333),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 64.dp
                )
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Username/email")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDDDDDD),
                focusedContainerColor = Color(0xFFDDDDDD),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF111111),
                unfocusedLabelColor = Color(0xFF555555),
                focusedLabelColor = Color(0xFF111111)
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 28.dp
                )
        )
        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = "Toggle password"
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDDDDDD),
                focusedContainerColor = Color(0xFFDDDDDD),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF111111),
                unfocusedLabelColor = Color(0xFF555555),
                focusedLabelColor = Color(0xFF111111)
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 18.dp
                )
        )
        Button(
            onClick = {
                onGetStartedClick(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp
                ),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF111111),
                contentColor = Color.White
            ),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp)
                )
            } else {
                Text(
                    text = "Get Started",
                    fontSize = 12.sp
                )
            }
        }
        Text(
            text = "forgot password?",
            fontSize = 11.sp,
            color = Color(0xFF222222),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 22.dp)
                .clickable {
                    onForgotPasswordClick()
                }
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New member? ",
                fontSize = 15.sp,
                color = Color(0xFF111111)
            )
            Text(
                text = "Register",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLoading) Color.Gray else Color(0xFF022BFF),
                modifier = Modifier.then(
                    if (!isLoading) {
                        Modifier.clickable {
                            onRegisterClick()
                        }
                    } else {
                        Modifier
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GetStartedScreenPreview() {
    BooksRepositoryAppTheme {
        GetStartedScreen (
            onBackClick = {},
            onGetStartedClick = { username, password -> } ,
            onForgotPasswordClick = {},
            onRegisterClick = {},
            getStartedState = GetStartedState.Idle
        )
    }
}