package com.example.booksrepositoryapp.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Default
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.booksrepositoryapp.navigation.routes.Routes
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsEffect
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsViewModel
import com.example.booksrepositoryapp.ui.addToCart.AddToCartScreen
import com.example.booksrepositoryapp.ui.addToCart.AddToCartViewModel
import com.example.booksrepositoryapp.ui.addressScreen.AddressListViewModel
import com.example.booksrepositoryapp.ui.addressScreen.AddressScreen
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedEffect
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedScreen
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterEffect
import com.example.booksrepositoryapp.ui.auth.register.RegisterScreen
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.bookCategory.BookCategoryScreen
import com.example.booksrepositoryapp.ui.bookCategory.BooksCategoryViewModel
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsScreen
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsViewModel
import com.example.booksrepositoryapp.ui.booksList.BooksListScreen
import com.example.booksrepositoryapp.ui.booksList.BooksListViewModel
import com.example.booksrepositoryapp.ui.checkout.CheckoutEffect
import com.example.booksrepositoryapp.ui.checkout.CheckoutScreen
import com.example.booksrepositoryapp.ui.checkout.CheckoutViewModel
import com.example.booksrepositoryapp.ui.landingPage.LandingPage
import com.example.booksrepositoryapp.ui.maintenancePage.MaintenanceScreen
import com.example.booksrepositoryapp.ui.successPayment.SuccessScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier,
) {
    val remoteConfig = Firebase.remoteConfig
    LaunchedEffect(Unit) {
        val configSettings =
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "max_addresses" to 5L,
                "checkout_enabled" to true,
                "shipping_fee" to 5.0,
                "is_maintenance_mode" to false,
            ),
        )
        remoteConfig.fetchAndActivate()
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomBarRoutes =
        setOf(
            Routes.BooksCategory.route,
            Routes.BooksList.route,
            Routes.AddToCart.route,
            Routes.Account.route,
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
                    enter =
                        slideInVertically(
                            initialOffsetY = { it },
                        ) + fadeIn(),
                    exit =
                        slideOutVertically(
                            targetOffsetY = { it },
                        ) + fadeOut(),
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
                                    Default.Home,
                                    contentDescription = "Home",
                                )
                            },
                            label = {
                                Text("Home")
                            },
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
                                    Default.ShoppingCart,
                                    contentDescription = "Cart",
                                )
                            },
                            label = {
                                Text("Cart")
                            },
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
                                    Default.Person,
                                    contentDescription = "Account",
                                )
                            },
                            label = {
                                Text("Account")
                            },
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination =
                    if (isLoggedIn) {
                        Routes.BooksCategory.route
                    } else {
                        Routes.LandingPage.route
                    },
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(Routes.LandingPage.route) {
                    LandingPage(
                        onRegisterClick = {
                            navController.navigate(Routes.Register.route)
                        },
                        onGetStartedClick = {
                            navController.navigate(Routes.GetStarted.route)
                        },
                    )
                }
                composable(Routes.GetStarted.route) {
                    val viewModel: GetStartedViewModel = koinViewModel()
                    GetStartedScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            when (effect) {
                                GetStartedEffect.NavigateToHome -> {
                                    navController.navigate(Routes.BooksCategory.route) {
                                        popUpTo(Routes.LandingPage.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                GetStartedEffect.NavigateToRegister -> {
                                    navController.navigate(Routes.Register.route) {
                                        popUpTo(Routes.GetStarted.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                GetStartedEffect.NavigateBack -> {
                                    navController.popBackStack()
                                }
                                else -> {}
                            }
                        }
                    )
                }
                composable(Routes.Register.route) {
                    val viewModel: RegisterViewModel = koinViewModel()
                    RegisterScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            when (effect) {
                                RegisterEffect.NavigateToHome -> {
                                    navController.navigate(Routes.BooksCategory.route) {
                                        popUpTo(Routes.LandingPage.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                RegisterEffect.NavigateToGetStarted -> {
                                    navController.navigate(Routes.GetStarted.route) {
                                        popUpTo(Routes.Register.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                RegisterEffect.NavigateBack -> {
                                    navController.popBackStack()
                                }
                                else -> {}
                            }
                        },
                    )
                }
                composable(Routes.BooksCategory.route) {
                    val viewModel: BooksCategoryViewModel = koinViewModel()
                    BookCategoryScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            navController.navigate(
                                Routes.BooksList.createRoute(effect.apiValue, effect.title),
                            )
                        },
                    )
                }
                composable(Routes.BooksList.route) {
                    val viewModel: BooksListViewModel = koinViewModel()
                    BooksListScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onNavigate = { effect ->
                            val cleanedWorkId = Uri.encode(effect.workId)
                            navController.navigate(
                                Routes.BookDetails.createRoute(cleanedWorkId),
                            )
                        },
                    )
                }
                composable(Routes.BookDetails.route) {
                    val viewModel: BookDetailsViewModel = koinViewModel()
                    BookDetailsScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.popBackStack()
                        },
                    )
                }
                composable(Routes.AddToCart.route) {
                    val viewModel: AddToCartViewModel = koinViewModel()
                    AddToCartScreen(
                        viewModel = viewModel,
                        shippingFee = remoteConfig.getDouble("shipping_fee"),
                        onNavigate = { effect ->
                            navController.navigate(
                                Routes.Checkout.createRoute(effect.total),
                            )
                        },
                    )
                }
                composable(Routes.Checkout.route) { backStackEntry ->
                    val total = backStackEntry.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0
                    val viewModel: CheckoutViewModel = koinViewModel()
                    CheckoutScreen(
                        viewModel = viewModel,
                        total = total,
                        isCheckoutEnabled = remoteConfig.getBoolean("checkout_enabled"),
                        onNavigate = { effect ->
                            when (effect) {
                                CheckoutEffect.NavigateBack -> navController.popBackStack()
                                CheckoutEffect.NavigateToAddressList -> navController.navigate(Routes.AddressList.route)
                                CheckoutEffect.NavigateToSuccess -> {
                                    navController.navigate(Routes.Success.route) {
                                        popUpTo(Routes.BooksCategory.route) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                                else -> {}
                            }
                        },
                    )
                }
                composable(Routes.Success.route) {
                    SuccessScreen(
                        onGoToHome = {
                            navController.popBackStack()
                        },
                    )
                }
                composable(Routes.AddressList.route) {
                    val viewModel: AddressListViewModel = koinViewModel()
                    AddressScreen(
                        viewModel = viewModel,
                        maxAddresses = remoteConfig.getLong("max_addresses"),
                        onBackClick = {
                            navController.popBackStack()
                        },
                    )
                }
                composable(Routes.Account.route) {
                    val viewModel: AccountDetailsViewModel = koinViewModel()
                    AccountDetailsScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            if (effect is AccountDetailsEffect.NavigateToLandingPage) {
                                navController.navigate(Routes.LandingPage.route) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}