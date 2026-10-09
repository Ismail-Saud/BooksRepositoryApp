package com.example.booksrepositoryapp.navigation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.booksrepositoryapp.navigation.routes.Route
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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation(
    isLoggedIn: Boolean = false,
    shippingFee: Double = 5.0,
    maxAddresses: Long = 5L,
    isCheckoutEnabled: Boolean = true,
    isMaintenanceMode: Boolean = false,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    if (isMaintenanceMode) {
        MaintenanceScreen()
        return
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val isBottomBarVisible = currentDestination?.let { dest ->
        dest.hasRoute<Route.BooksCategory>() ||
                dest.hasRoute<Route.BooksList>() ||
                dest.hasRoute<Route.AddToCart>() ||
                dest.hasRoute<Route.Account>()
    } ?: false

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            ) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<Route.BooksCategory>() == true,
                        onClick = {
                            navController.navigate(Route.BooksCategory) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo<Route.BooksCategory> { saveState = true }
                            }
                        },
                        icon = { Icon(Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<Route.AddToCart>() == true,
                        onClick = {
                            navController.navigate(Route.AddToCart) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo<Route.BooksCategory> { saveState = true }
                            }
                        },
                        icon = { Icon(Default.ShoppingCart, contentDescription = "Cart") },
                        label = { Text("Cart") },
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<Route.Account>() == true,
                        onClick = {
                            navController.navigate(Route.Account) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo<Route.BooksCategory> { saveState = true }
                            }
                        },
                        icon = { Icon(Default.Person, contentDescription = "Account") },
                        label = { Text("Account") },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Route.BooksCategory else Route.LandingPage,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Route.LandingPage> {
                LandingPage(
                    onRegisterClick = { navController.navigate(Route.Register) },
                    onGetStartedClick = { navController.navigate(Route.GetStarted) },
                )
            }

            composable<Route.GetStarted> {
                val viewModel: GetStartedViewModel = koinViewModel()
                GetStartedScreen(
                    viewModel = viewModel,
                    onNavigate = { effect ->
                        when (effect) {
                            GetStartedEffect.NavigateToHome -> {
                                navController.navigate(Route.BooksCategory) {
                                    popUpTo<Route.LandingPage> { inclusive = true }
                                }
                            }
                            GetStartedEffect.NavigateToRegister -> {
                                navController.navigate(Route.Register) {
                                    popUpTo<Route.GetStarted> { inclusive = true }
                                }
                            }
                            GetStartedEffect.NavigateBack -> navController.popBackStack()
                            else -> {}
                        }
                    },
                )
            }

            composable<Route.Register> {
                val viewModel: RegisterViewModel = koinViewModel()
                RegisterScreen(
                    viewModel = viewModel,
                    onNavigate = { effect ->
                        when (effect) {
                            RegisterEffect.NavigateToHome -> {
                                navController.navigate(Route.BooksCategory) {
                                    popUpTo<Route.LandingPage> { inclusive = true }
                                }
                            }
                            RegisterEffect.NavigateToGetStarted -> {
                                navController.navigate(Route.GetStarted) {
                                    popUpTo<Route.Register> { inclusive = true }
                                }
                            }
                            RegisterEffect.NavigateBack -> navController.popBackStack()
                            else -> {}
                        }
                    },
                )
            }

            composable<Route.BooksCategory> { backStackEntry ->
                val viewModel: BooksCategoryViewModel = koinViewModel()
                BookCategoryScreen(
                    viewModel = viewModel,
                    onNavigate = { effect ->
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.navigate(Route.BooksList(effect.apiValue, effect.title))
                        }
                    },
                )
            }

            composable<Route.BooksList> { backStackEntry ->
                val viewModel: BooksListViewModel = koinViewModel()
                BooksListScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.popBackStack()
                        }
                    },
                    onNavigate = { effect ->
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.navigate(Route.BookDetails(effect.workId))
                        }
                    },
                )
            }

            composable<Route.BookDetails> { backStackEntry ->
                val viewModel: BookDetailsViewModel = koinViewModel()
                BookDetailsScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.popBackStack()
                        }
                    },
                )
            }

            composable<Route.AddToCart> { backStackEntry ->
                val viewModel: AddToCartViewModel = koinViewModel()
                AddToCartScreen(
                    viewModel = viewModel,
                    shippingFee = shippingFee,
                    onNavigate = { effect ->
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.navigate(Route.Checkout(effect.total.toString()))
                        }
                    },
                )
            }

            composable<Route.Checkout> { backStackEntry ->
                val viewModel: CheckoutViewModel = koinViewModel(
                    parameters = { parametersOf(shippingFee) }
                )
                CheckoutScreen(
                    viewModel = viewModel,
                    isCheckoutEnabled = isCheckoutEnabled,
                    onNavigate = { effect ->
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            when (effect) {
                                CheckoutEffect.NavigateBack -> navController.popBackStack()
                                CheckoutEffect.NavigateToAddressList -> navController.navigate(Route.AddressList)
                                CheckoutEffect.NavigateToSuccess -> {
                                    navController.navigate(Route.Success) {
                                        popUpTo<Route.BooksCategory> { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                                else -> {}
                            }
                        }
                    },
                )
            }

            composable<Route.Success> { backStackEntry ->
                SuccessScreen(
                    onGoToHome = {
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.popBackStack()
                        }
                    },
                )
            }

            composable<Route.AddressList> { backStackEntry ->
                val viewModel: AddressListViewModel = koinViewModel()
                AddressScreen(
                    viewModel = viewModel,
                    maxAddresses = maxAddresses,
                    onBackClick = {
                        if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.popBackStack()
                        }
                    },
                )
            }

            composable<Route.Account> {
                val viewModel: AccountDetailsViewModel = koinViewModel()
                AccountDetailsScreen(
                    viewModel = viewModel,
                    onNavigate = { effect ->
                        if (effect is AccountDetailsEffect.NavigateToLandingPage) {
                            navController.navigate(Route.LandingPage) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                )
            }
        }
    }
}