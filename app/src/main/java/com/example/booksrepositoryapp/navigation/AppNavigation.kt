package com.example.booksrepositoryapp.navigation

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.booksrepositoryapp.MainActivityViewModel
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import com.example.booksrepositoryapp.navigation.routes.Routes
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsViewModel
import com.example.booksrepositoryapp.ui.address_screen.AddressListViewModel
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
import com.example.booksrepositoryapp.ui.loading_screen.LoadingScreenCompose
import com.example.booksrepositoryapp.ui.success_payment.SuccessScreenCompose
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val mainActivityViewModel: MainActivityViewModel = viewModel()
    val bottomBarRoutes = setOf(
        Routes.BooksCategory.route,
        Routes.BooksList.route,
        Routes.AddToCart.route,
        Routes.Account.route
    )
    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.BooksCategory.route,
                        onClick = {
                            navController.navigate(Routes.BooksCategory.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(Routes.BooksCategory.route) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text("Home")
                        }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.AddToCart.route,
                        onClick = {
                            navController.navigate(Routes.AddToCart.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(Routes.BooksCategory.route) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        },
                        label = {
                            Text("Cart")
                        }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.Account.route,
                        onClick = {
                            navController.navigate(Routes.Account.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(Routes.BooksCategory.route) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Account"
                            )
                        },
                        label = {
                            Text("Account")
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (mainActivityViewModel.isLoggedIn.collectAsState().value) {
                Routes.BooksCategory.route
            } else {
                Routes.LandingPage.route
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LandingPage.route) {
                LandingPageScreen(
                    onRegisterClick = {
                        navController.navigate(Routes.Register.route)
                    },
                    onGetStartedClick = {
                        navController.navigate(Routes.GetStarted.route)
                    }
                )
            }
            composable(Routes.GetStarted.route) {
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
                                navController.navigate(Routes.BooksCategory.route)
                            }
                        }
                    }
                }
                GetStartedScreen(
                    onBackClick = {
                        navController.navigate(Routes.LandingPage.route)
                    },
                    onRegisterClick = {
                        navController.navigate(Routes.Register.route)
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
            composable(Routes.Register.route) {
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
                                navController.navigate(Routes.BooksCategory.route)
                            }
                        }
                    }
                }
                RegisterScreen(
                    onBackClick = {
                        navController.navigate(Routes.LandingPage.route)
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
                        navController.navigate(Routes.GetStarted.route)
                    }
                )
            }
            composable(Routes.BooksCategory.route) {
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
                                    Routes.BooksList.createRoute(category.apiValue, category.title)
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
            composable(Routes.BooksList.route) { backStackEntry ->
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
                        val cleanedWorkId = Uri.encode(workId)
                        navController.navigate(
                            Routes.BookDetails.createRoute(cleanedWorkId)
                        )
                    }
                )
            }
            composable(Routes.BookDetails.route) { backStackEntry ->
                val workId = backStackEntry.arguments?.getString("workId") ?: "Unknown"
                val viewModel: BookDetailsViewModel = viewModel()
                val context = LocalContext.current
                LaunchedEffect(workId) {
                    viewModel.getBookDetails(workId)
                }
                val state by viewModel.bookDetailState.collectAsState()
                when (val currentState = state) {
                    BookDetailsState.Idle -> {}
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
                }
            }
            composable(Routes.AddToCart.route) {
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
                                    Routes.Checkout.createRoute(total)
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
            composable(Routes.Checkout.route) { backStackEntry ->
                val total = backStackEntry.arguments
                    ?.getString("total")
                    ?.toDoubleOrNull() ?: 0.0
                val viewModel: CheckoutViewModel = viewModel()
                val context = LocalContext.current
                val checkoutState by viewModel.checkoutState.collectAsState()
                var showLoadingDialog by remember {
                    mutableStateOf(false)
                }
                val selectedAddress = when (val state = checkoutState) {
                    is CheckoutState.Success -> state.address
                    is CheckoutState.Error -> null
                    CheckoutState.Idle -> null
                    CheckoutState.Loading -> null
                }
                CheckoutScreen(
                    total = total,
                    selectedAddress = selectedAddress,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onSelectAddressClick = {
                        navController.navigate(Routes.AddressList.route)
                    },
                    onPayClick = {
                        showLoadingDialog = true
                    },
                    viewModel = viewModel
                )

                LoadingScreenCompose(
                    showDialog = showLoadingDialog
                )

                if (showLoadingDialog) {
                    LaunchedEffect(Unit) {
                        delay(2000)
                        viewModel.clearCart()
                        showLoadingDialog = false
                        navController.navigate(Routes.Success.route)
                    }
                }
                if (checkoutState is CheckoutState.Error) {
                    val message = (checkoutState as CheckoutState.Error).message
                    LaunchedEffect(message) {
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            composable(Routes.Success.route){
                SuccessScreenCompose(
                    onGoToHome = {
                        navController.navigate(Routes.BooksCategory.route) {
                            popUpTo(Routes.AddToCart.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.AddressList.route) {
                val viewModel: AddressListViewModel = viewModel()
                val context = LocalContext.current
                var showConfirmation by remember {
                    mutableStateOf(false)
                }
                val addressCount by viewModel.addressCount.collectAsStateWithLifecycle(initialValue = 0)
                AddressScreenCompose(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onAddClick = {
                        if (addressCount < 5) {
                            viewModel.addEmptyAddress()
                        }
                        else {
                            Toast.makeText(context, "Max Addresses", Toast.LENGTH_SHORT).show()

                        }
                    },
                    onDeleteClick = {
                        if (addressCount > 0) {
                            showConfirmation = true
                        }
                        else {
                            Toast.makeText(context, "No Address Found", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                if (showConfirmation && addressCount > 0) {
                    ConfirmationBottomSheetCompose(
                        title = "Delete All Addresses",
                        message = "Are you sure to delete all addresses?",
                        positiveButtonText = "Delete",
                        onConfirm = {
                            viewModel.deleteAllAddresses()
                            showConfirmation = false
                        },
                        onDismiss = {
                            showConfirmation = false
                        }
                    )
                }
            }
            composable(Routes.Account.route) {
                val viewModel: AccountDetailsViewModel = viewModel()
                val context = LocalContext.current
                val accountState by viewModel.userState.collectAsState()
                AccountDetailsScreen(
                    viewModel = viewModel(),
                    onLogoutClick = {
                        viewModel.logout()
                        navController.navigate(Routes.LandingPage.route) {
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                )
            }
        }
    }

}
