package com.example.booksrepositoryapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.booksrepositoryapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.booksrepositoryapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainActivityViewModel by viewModels()
    private var backPressedTime = 0L


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navControllerCompose = rememberNavController()
            AppNavigation(
                navController = navControllerCompose,
                modifier = Modifier.safeDrawingPadding()
            )
        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
//        if (savedInstanceState == null) {
//            checkLoginState()
//        }
//        observeLoginState()
//        setupBottomNavigation()
//        setupBottomNavVisibility()
//        setupBackPress()
    }

    private fun checkLoginState() {
        val destination = if (viewModel.isLoggedIn.value) {
            R.id.app_graph
        } else {
            R.id.auth_graph
        }
        navController.navigate(destination, null,
            navOptions {
                launchSingleTop = true
                popUpTo(R.id.main_nav_graph) {
                    inclusive = true
                }
            }
        )
    }

    private fun setupBottomNavigation(){
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.home_graph -> {
                    navController.navigate(
                        R.id.booksCategoryFragment,
                        null,
                        navOptions {
                            popUpTo(R.id.booksCategoryFragment){
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    )
                    true
                }
                R.id.cart_graph -> {
                    navController.navigate(
                        R.id.addToCartFragment,
                        null,
                        navOptions {
                            popUpTo(R.id.booksCategoryFragment){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    )
                    true
                }
                R.id.account_graph -> {
                    navController.navigate(
                        R.id.accountDetailsFragment,
                        null,
                        navOptions {
                            popUpTo(R.id.booksCategoryFragment){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun setupBottomNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.landingPageFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.getStartedFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.registerFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.booksDetailsFragment,
                R.id.checkoutFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.successFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.addressListFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
            when(destination.id) {
                R.id.booksCategoryFragment -> {
                    binding.bottomNav.menu
                        .findItem(R.id.home_graph)
                        .isChecked = true
                }
                R.id.booksListFragment -> {
                    binding.bottomNav.menu
                        .findItem(R.id.home_graph)
                        .isChecked = true
                }
                R.id.booksDetailsFragment -> {
                    binding.bottomNav.menu
                        .findItem(R.id.home_graph)
                        .isChecked = true
                }
                R.id.addToCartFragment -> {
                    binding.bottomNav.menu
                        .findItem(R.id.cart_graph)
                        .isChecked = true
                }
                R.id.accountDetailsFragment -> {
                    binding.bottomNav.menu
                        .findItem(R.id.account_graph)
                        .isChecked = true
                }
            }
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoggedIn.collect { loggedIn ->
                    binding.bottomNav.visibility =
                        if(loggedIn)
                            View.VISIBLE
                        else
                            View.GONE
                }
            }
        }
    }
    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            val currentDestination = navController.currentDestination?.id
            when (currentDestination) {
                R.id.booksCategoryFragment,
                R.id.landingPageFragment -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        finish()
                    } else {
                        backPressedTime = currentTime
                        Toast.makeText(
                            this@MainActivity,
                            "Press back again to exit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else -> {
                    if (!navController.popBackStack()) {
                        finish()
                    }
                }
            }
        }
    }

    fun selectBottomNavItem(itemId:Int){
        binding.bottomNav.selectedItemId = itemId
    }
}