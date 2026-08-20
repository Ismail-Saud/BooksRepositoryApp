package com.example.booksrepositoryapp.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.address_screen.AddressScreenCompose
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedScreen
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedState
import com.example.booksrepositoryapp.ui.auth.getstarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterScreen
import com.example.booksrepositoryapp.ui.auth.register.RegisterState
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.book_category.BookCategoryScreen
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryState
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryViewModel
import com.example.booksrepositoryapp.ui.book_details.BookDetailsScreenCompose
import com.example.booksrepositoryapp.ui.book_details.BookDetailsState
import com.example.booksrepositoryapp.ui.book_details.BookDetailsViewModel
import com.example.booksrepositoryapp.ui.books_screen.BooksListScreen
import com.example.booksrepositoryapp.ui.books_screen.BooksListState
import com.example.booksrepositoryapp.ui.books_screen.BooksListViewModel
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartScreen
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartState
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartViewModel
import com.example.booksrepositoryapp.ui.checkout_screen.CheckoutScreen
import com.example.booksrepositoryapp.ui.checkout_screen.CheckoutState
import com.example.booksrepositoryapp.ui.checkout_screen.CheckoutViewModel
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheet
import com.example.booksrepositoryapp.ui.conformation_bottom_sheet.ConfirmationBottomSheetCompose
import com.example.booksrepositoryapp.ui.landingpage.LandingPageScreen
import kotlinx.coroutines.launch
import kotlin.getValue

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
        composable("books") {
            val viewModel: BooksCategoryViewModel = viewModel()
            val context = LocalContext.current
            val state by viewModel.categoryState.observeAsState(
                BooksCategoryState.Idle
            )
            when (val currentState = state) {
                BooksCategoryState.Idle -> {}
                BooksCategoryState.Loading -> {}
                is BooksCategoryState.Success -> {
                    BookCategoryScreen(
                        categories = currentState.categories,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onSearch = { query ->
                            viewModel.searchTodos(query)
                        },
                        onCardClick = { category ->
                            navController.navigate(
                                "books_list/${category.apiValue}/${category.title}"
                            )
                        }
                    )
                }
                is BooksCategoryState.Error -> {
                    Toast.makeText(
                        context,
                        currentState.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetState()
                }
            }
        }
        composable("books_list/{apiValue}/{title}") { backStackEntry ->
            val apiValue = backStackEntry.arguments?.getString("apiValue") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: "Unknown"
            val viewModel: BooksListViewModel = viewModel()
            BooksListScreen(
                apiValue = apiValue,
                title = title,
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onBookClick = { workId ->
                    navController.navigate("book_details/$workId")
                }
            )
        }
        composable("book_details/{workId}") { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId") ?: "Unknown"
            val viewModel: BookDetailsViewModel = viewModel()
            val context = LocalContext.current
            LaunchedEffect(workId) {
                viewModel.getBookDetails(workId)
            }
            val state by viewModel.bookDetailState.collectAsState()
            when (val currentState = state) {
                BooksListState.Idle -> {}
                is BookDetailsState.Loading -> {}
                is BookDetailsState.Success -> {
                    currentState.books?.let { book ->
                        BookDetailsScreenCompose(
                            book = book,
                            onBackClick = {
                                navController.navigateUp()
                            },
                            onAddToCartClick = {
                                viewModel.addToCart(workId)
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                is BookDetailsState.Error -> {
                    Toast.makeText(context, currentState.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
        composable("add_to_cart") {
            val viewModel: AddToCartViewModel = viewModel()
            val context = LocalContext.current
            val state by viewModel.addToCartState.collectAsState()
            var showConfirmation by remember {
                mutableStateOf(false)
            }
            var selectedCartItem by remember {
                mutableStateOf<CartItem?>(null)
            }
            LaunchedEffect(Unit) {
                viewModel.getCartItems()
            }
            when (val currentState = state) {
                AddToCartState.Idle -> {}
                AddToCartState.Loading -> {}
                is AddToCartState.Error -> {
                    Toast.makeText(
                        context,
                        currentState.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is AddToCartState.Success -> {
                    AddToCartScreen(
                        cartItems = currentState.cartItem,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onIncreaseClick = { cartItem ->
                            viewModel.increaseQuantity(cartItem)
                        },
                        onDecreaseClick = { cartItem ->
                            if (cartItem.quantity > 1) {
                                viewModel.decreaseQuantity(cartItem)
                            } else {
                                selectedCartItem = cartItem
                                showConfirmation = true
                            }
                        },
                        onRemoveClick = { cartItem ->
                            selectedCartItem = cartItem
                            showConfirmation = true
                        },
                        onCheckoutClick = { total ->
                            navController.navigate(
                                "checkout/$total"
                            )
                        }
                    )
                    if (showConfirmation && selectedCartItem != null) {
                        ConfirmationBottomSheetCompose(
                            title = "Remove Item",
                            message = "Remove this item from your cart?",
                            positiveButtonText = "Remove",
                            onConfirm = {
                                selectedCartItem?.let { cartItem ->
                                    viewModel.removeCartItem(cartItem)
                                }
                                selectedCartItem = null
                                showConfirmation = false
                            },
                            onDismiss = {
                                selectedCartItem = null
                                showConfirmation = false
                            }
                        )
                    }
                }
            }
        }
        composable("checkout/{total}") { backStackEntry ->
            val total = backStackEntry.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0
            val viewModel: CheckoutViewModel = viewModel()
            val context = LocalContext.current
            val checkoutState by viewModel.checkoutState.collectAsState()
            when (val state = checkoutState) {
                CheckoutState.Idle -> {
                    CheckoutScreen(
                        total = total,
                        selectedAddress = null,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onSelectAddressClick = {
                            navController.navigate("address_list")
                        },
                        onPayClick = {
                            viewModel.clearCart()
                            navController.navigate("success")
                        },
                        viewModel = viewModel
                    )
                }
                CheckoutState.Loading -> {}
                is CheckoutState.Success -> {
                    CheckoutScreen(
                        total = total,
                        selectedAddress = state.address,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onSelectAddressClick = {
                            navController.navigate("address_list")
                        },
                        onPayClick = {
                            viewModel.clearCart()
                            navController.navigate("success")
                        },
                        viewModel = viewModel
                    )
                }
                is CheckoutState.Error -> {
                    CheckoutScreen(
                        total = total,
                        selectedAddress = null,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onSelectAddressClick = {
                            navController.navigate("address_list")
                        },
                        onPayClick = {
                            viewModel.clearCart()
                            navController.navigate("success")
                        },
                        viewModel = viewModel
                    )
                    LaunchedEffect(state.message) {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
        composable("account") {
            AccountDetailsScreen()
        }
    }
}
