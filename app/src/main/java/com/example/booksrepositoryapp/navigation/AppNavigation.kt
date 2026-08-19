package com.example.booksrepositoryapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.address_screen.AddressScreenCompose
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedScreen
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedState
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterScreen
import com.example.booksrepositoryapp.ui.auth.register.RegisterState
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.book_category.BookCategoryScreen
import com.example.booksrepositoryapp.ui.book_details.BookDetailsScreenCompose
import com.example.booksrepositoryapp.ui.books_screen.BooksListScreen
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartScreen
import com.example.booksrepositoryapp.ui.checkout_screen.CheckoutScreen
import com.example.booksrepositoryapp.ui.landingpage.LandingPageScreen
import kotlinx.coroutines.launch
import kotlin.getValue

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("books") {
            BookCategoryScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onCardClick = {
                    navController.navigate("books_list")
                }
            )
        }
        composable("books_list") {
            BooksListScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable("landing_page") {

            LandingPageScreen(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onGetStartedClick = {
                    navController.navigate("get_started")
                }
            )
        }
        composable("get_started") {
            val viewModel: GetStartedViewModel = viewModel()
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.getStartedState.collect { state ->
                    when (state) {
                        GetStartedState.Idle -> {}
                        is GetStartedState.Error -> {
                            Toast.makeText(
                                context,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        GetStartedState.Success -> {
                            navController.navigate("app")
                        }
                    }
                }
            }
            GetStartedScreen(
                onBackClick = {
                    navController.navigate("landing_page")
                },
                onRegisterClick = {
                    navController.navigate("register")
                },
                onForgotPasswordClick = {},
                onGetStartedClick = { email, password ->
                    viewModel.login(
                        email = email,
                        password = password
                    )
                }
            )
        }
        composable("register") {
            val viewModel: RegisterViewModel = viewModel()
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.registerUser.collect { state ->
                    when (state) {
                        RegisterState.Idle -> {}
                        is RegisterState.Error -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        }
                        RegisterState.Success -> {
                            navController.navigate("app")
                        }
                    }
                }
            }
            RegisterScreen(
                onBackClick = {
                    navController.navigate("landing_page")
                },
                onRegisterClick = { username, email, password, confirmPassword ->
                    viewModel.viewModelScope.launch {
                        viewModel.register(
                            userName = username,
                            email = email,
                            password = password,
                            confirmPass = confirmPassword
                        )
                    }
                },
                onGetStartedClick = {
                    navController.navigate("get_started")
                }
            )
        }
        composable("book_details") {
            BookDetailsScreenCompose(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable("cart") {
            AddToCartScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onCheckoutClick = {
                    navController.navigate("checkout")
                }
            )
        }
        composable("account") {
            AccountDetailsScreen()
        }
        composable("address") {
            AddressScreenCompose(
                onBackClick = {
                    navController.navigateUp()
                },
                onAddClick = {},
                onDeleteClick = {}
            )
        }
        composable("checkout") {
            CheckoutScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onPayClick = {},
                onSelectAddressClick = {

                },
            )
        }
    }
}