package com.example.booksrepositoryapp.navigation

import android.net.Uri
import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.booksrepositoryapp.navigation.routes.Routes
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsEffect
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsScreen
import com.example.booksrepositoryapp.ui.addToCart.AddToCartScreen
import com.example.booksrepositoryapp.ui.addressScreen.AddressScreenCompose
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedEffect
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedScreen
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterEffect
import com.example.booksrepositoryapp.ui.auth.register.RegisterScreen
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.bookCategory.BookCategoryScreen
import com.example.booksrepositoryapp.ui.bookCategory.BooksCategoryViewModel
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsScreenCompose
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsViewModel
import com.example.booksrepositoryapp.ui.booksList.BooksListScreen
import com.example.booksrepositoryapp.ui.booksList.BooksListViewModel
import com.example.booksrepositoryapp.ui.checkout.CheckoutEffect
import com.example.booksrepositoryapp.ui.checkout.CheckoutScreen
import com.example.booksrepositoryapp.ui.landingPage.LandingPageScreen
import com.example.booksrepositoryapp.ui.maintenancePage.MaintenanceScreen
import com.example.booksrepositoryapp.ui.successPayment.SuccessScreenCompose
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

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
            "max_addresses" to 5L,
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
                    GetStartedScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            when (effect) {
                                GetStartedEffect.NavigateToHome -> {
                                    navController.navigate(Routes.BooksCategory.route) {
                                        popUpTo(Routes.Register.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                GetStartedEffect.NavigateToRegister -> navController.navigate(Routes.Register.route)
                                GetStartedEffect.NavigateBack -> navController.navigate(Routes.LandingPage.route)
                                else -> {}
                            }
                        }
                    )
                }
                composable(Routes.Register.route) {
                    val viewModel: RegisterViewModel = viewModel()
                    RegisterScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            when (effect) {
                                RegisterEffect.NavigateToHome -> {
                                    navController.navigate(Routes.BooksCategory.route) {
                                        popUpTo(Routes.Register.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                                RegisterEffect.NavigateToGetStarted -> navController.navigate(Routes.GetStarted.route)
                                RegisterEffect.NavigateBack -> navController.navigate(Routes.LandingPage.route)
                                else -> {}
                            }
                        }
                    )
                }
                composable(Routes.BooksCategory.route) {
                    val viewModel: BooksCategoryViewModel = viewModel()
                    BookCategoryScreen(
                        viewModel = viewModel,
                        onNavigate = { effect ->
                            navController.navigate(
                                Routes.BooksList.createRoute(effect.apiValue, effect.title)
                            )
                        }
                    )
                }
                composable(Routes.BooksList.route) {
                    val viewModel: BooksListViewModel = viewModel()
                    BooksListScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onNavigate = { effect ->
                            val cleanedWorkId = Uri.encode(effect.workId)
                            navController.navigate(
                                Routes.BookDetails.createRoute(cleanedWorkId)
                            )
                        }
                    )
                }
                composable(Routes.BookDetails.route) {
                    val viewModel: BookDetailsViewModel = viewModel()
                    BookDetailsScreenCompose(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
                composable(Routes.AddToCart.route) {
                    AddToCartScreen(
                        viewModel = viewModel(),
                        shippingFee = remoteConfig.getDouble("shipping_fee"),
                        onNavigate = { effect ->
                            navController.navigate(
                                Routes.Checkout.createRoute(effect.total)
                            )
                        }
                    )
                }
                composable(Routes.Checkout.route) { backStackEntry ->
                    val total = backStackEntry.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0
                    CheckoutScreen(
                        viewModel = viewModel(),
                        total = total,
                        isCheckoutEnabled = remoteConfig.getBoolean("checkout_enabled"),
                        onNavigate = { effect ->
                            when (effect) {
                                CheckoutEffect.NavigateBack -> navController.navigateUp()
                                CheckoutEffect.NavigateToAddressList -> navController.navigate(Routes.AddressList.route)
                                CheckoutEffect.NavigateToSuccess -> navController.navigate(Routes.Success.route)
                                else -> {}
                            }
                        }
                    )
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
                    AddressScreenCompose(
                        viewModel = viewModel(),
                        maxAddresses = remoteConfig.getLong("max_addresses"),
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
                composable(Routes.Account.route) {
                    AccountDetailsScreen(
                        viewModel = viewModel(),
                        onNavigate = { effect ->
                            if (effect is AccountDetailsEffect.NavigateToLandingPage) {
                                navController.navigate(Routes.LandingPage.route) {
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
