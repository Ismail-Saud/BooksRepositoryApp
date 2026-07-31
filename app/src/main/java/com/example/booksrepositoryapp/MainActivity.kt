package com.example.booksrepositoryapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.booksrepositoryapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainActivityViewModel by viewModels()
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        checkLoginState()
        observeLoginState()
        setupBottomNavigation()
        handleBackPress()
    }

    private fun checkLoginState() {
        val loggedIn = viewModel.isLoggedIn.value
        if (loggedIn) {
            navController.navigate(R.id.app_graph)
        } else {
            navController.navigate(R.id.auth_graph)
        }

    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    navController.navigate(R.id.home_graph)
                    true
                }
                R.id.nav_cart -> {
                    navController.navigate(R.id.cart_graph)
                    true
                }
                R.id.nav_account -> {
                    navController.navigate(R.id.account_graph)
                    true
                }
                else -> false
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

    private fun handleBackPress(){
        onBackPressedDispatcher.addCallback(this){
            if(navController.previousBackStackEntry != null){
                navController.navigateUp()
            }else{
                if(backPressedTime + 2000 > System.currentTimeMillis()){
                    finish()
                }else{
                    Toast.makeText(
                        this@MainActivity,
                        "Press back again to exit",
                        Toast.LENGTH_SHORT
                    ).show()
                    backPressedTime = System.currentTimeMillis()
                }
            }
        }
    }

    fun selectBottomNavItem(itemId:Int){
        binding.bottomNav.selectedItemId = itemId
    }
}