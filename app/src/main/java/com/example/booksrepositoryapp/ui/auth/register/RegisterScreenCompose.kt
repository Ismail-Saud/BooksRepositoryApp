package com.example.booksrepositoryapp.ui.auth.register

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.navigation.routes.Routes
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterClick: (String, String, String, String) -> Unit,
    onGetStartedClick: () -> Unit,
    registerState: RegisterState
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isLoading = registerState is RegisterState.Loading

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
                text = "Register",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111),
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = "Please fill your details to signup.",
            fontSize = 12.sp,
            color = Color(0xFF333333),
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 58.dp
                )
        )
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text("Username")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDDDDDD),
                focusedContainerColor = Color(0xFFDDDDDD),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color(0xFF111111),
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp
                )
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
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
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp
                )
                .testTag("email")
        )
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
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp
                )
                .testTag("password")
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text("Confirm Password")
            },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) {
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
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp
                )
        )
        Button(
            onClick = {
                onRegisterClick(username, email, password, confirmPassword)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
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
                    text = "Register",
                    fontSize = 12.sp
                )
            }
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already a member? ",
                fontSize = 15.sp,
                color = Color(0xFF111111)
            )
            Text(
                text = "SignIn",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLoading) Color.Gray else Color(0xFF022BFF),
                modifier = Modifier.then(
                    if (!isLoading) {
                        Modifier.clickable {
                            onGetStartedClick()
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
fun RegisterScreenPreview() {
    BooksRepositoryAppTheme {
        RegisterScreen (
            onBackClick = {},
            onGetStartedClick = {},
            onRegisterClick = { username, email, password, confirmPassword -> },
            registerState = RegisterState.Idle
        )
    }
}