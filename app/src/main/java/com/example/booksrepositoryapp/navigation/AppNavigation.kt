package com.example.booksrepositoryapp.navigation

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.navigation.routes.Routes
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsViewModel
import com.example.booksrepositoryapp.ui.addressScreen.AddressListViewModel
import com.example.booksrepositoryapp.ui.addressScreen.AddressScreenCompose
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedScreen
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedState
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterScreen
import com.example.booksrepositoryapp.ui.auth.register.RegisterState
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.bookCategory.BookCategoryScreen
import com.example.booksrepositoryapp.ui.bookCategory.BooksCategoryState
import com.example.booksrepositoryapp.ui.bookCategory.BooksCategoryViewModel
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsScreenCompose
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsState
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsViewModel
import com.example.booksrepositoryapp.ui.booksList.BooksListScreen
import com.example.booksrepositoryapp.ui.booksList.BooksListViewModel
import com.example.booksrepositoryapp.ui.addToCart.AddToCartScreen
import com.example.booksrepositoryapp.ui.addToCart.AddToCartState
import com.example.booksrepositoryapp.ui.addToCart.AddToCartViewModel
import com.example.booksrepositoryapp.ui.checkout.CheckoutScreen
import com.example.booksrepositoryapp.ui.checkout.CheckoutState
import com.example.booksrepositoryapp.ui.checkout.CheckoutViewModel
import com.example.booksrepositoryapp.ui.conformationBottomSheet.ConfirmationBottomSheetCompose
import com.example.booksrepositoryapp.ui.landingPage.LandingPageScreen
import com.example.booksrepositoryapp.ui.loading.LoadingScreenCompose
import com.example.booksrepositoryapp.ui.maintenancePage.MaintenanceScreen
import com.example.booksrepositoryapp.ui.successPayment.SuccessScreenCompose
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.delay

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    val remoteConfig = Firebase.remoteConfig
    LaunchedEffect(Unit) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(
            "max_addresses" to 4L,
            "checkout_enabled" to true,
            "shipping_fee" to 5.0,
            "is_maintenance_mode" to false
        ))
        remoteConfig.fetchAndActivate()
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomBarRoutes = setOf(
        Routes.BooksCategory.route,
        Routes.BooksList.route,
        Routes.AddToCart.route,
        Routes.Account.route
    )
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val isMaintenanceModeByRemote = remoteConfig.getBoolean("is_maintenance_mode")

    if (isMaintenanceModeByRemote) {
        MaintenanceScreen()
    } else {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = currentRoute in bottomBarRoutes,
                    enter = slideInVertically(
                        initialOffsetY = { it }
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it }
                    ) + fadeOut()
                ) {
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
                startDestination = if (isLoggedIn) {
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
                    val getStartedState by viewModel.getStartedState.collectAsState()
                    LaunchedEffect(getStartedState) {
                        when (getStartedState) {
                            GetStartedState.Idle -> {}
                            GetStartedState.Loading -> {}
                            is GetStartedState.Error -> {
                                Toast.makeText(
                                    context,
                                    (getStartedState as GetStartedState.Error).message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            GetStartedState.Success -> {
                                Toast.makeText(
                                    context,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Routes.BooksCategory.route)
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
                        },
                        getStartedState = getStartedState
                    )
                }
                composable(Routes.Register.route) {
                    val viewModel: RegisterViewModel = viewModel()
                    val context = LocalContext.current
                    val registerState by viewModel.registerUser.collectAsState()
                    LaunchedEffect(registerState) {
                        when (registerState) {
                            RegisterState.Idle -> {}
                            RegisterState.Loading -> {}
                            is RegisterState.Error -> {
                                Toast.makeText(
                                    context,
                                    (registerState as RegisterState.Error).message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            RegisterState.Success -> {
                                Toast.makeText(
                                    context,
                                    "Signup Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Routes.BooksCategory.route) {
                                    popUpTo(Routes.Register.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                    RegisterScreen(
                        onBackClick = {
                            navController.navigate(Routes.LandingPage.route)
                        },
                        onRegisterClick = { username, email, password, confirmPassword ->
                            viewModel.register(
                                username = username,
                                email = email,
                                password = password,
                                confirmPass = confirmPassword
                            )
                        },
                        onGetStartedClick = {
                            navController.navigate(Routes.GetStarted.route)
                        },
                        registerState = registerState
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
                    var selectedCart by remember {
                        mutableStateOf<Cart?>(null)
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
                                carts = currentState.cart,
                                onIncreaseClick = { cartItem ->
                                    viewModel.increaseQuantity(cartItem)
                                },
                                onDecreaseClick = { cartItem ->
                                    if (cartItem.quantity > 1) {
                                        viewModel.increaseQuantity(cartItem) // wait, should be decrease?
                                        // checking AddToCartViewModel... it has decreaseQuantity
                                        viewModel.decreaseQuantity(cartItem)
                                    } else {
                                        selectedCart = cartItem
                                        showConfirmation = true
                                    }
                                },
                                onRemoveClick = { cartItem ->
                                    selectedCart = cartItem
                                    showConfirmation = true
                                },
                                onCheckoutClick = { total ->
                                    navController.navigate(
                                        Routes.Checkout.createRoute(total)
                                    )
                                },
                                shippingFee = remoteConfig.getDouble("shipping_fee")
                            )
                            if (showConfirmation && selectedCart != null) {
                                ConfirmationBottomSheetCompose(
                                    title = "Remove Item",
                                    message = "Remove this item from your cart?",
                                    positiveButtonText = "Remove",
                                    onConfirm = {
                                        selectedCart?.let { cartItem ->
                                            viewModel.removeCartItem(cartItem)
                                        }
                                        selectedCart = null
                                        showConfirmation = false
                                    },
                                    onDismiss = {
                                        selectedCart = null
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
                        viewModel = viewModel,
                        isCheckoutEnabled = remoteConfig.getBoolean("checkout_enabled")
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
                            if (addressCount < remoteConfig.getLong("max_addresses")) {
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
                    AccountDetailsScreen(
                        viewModel = viewModel,
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
}
