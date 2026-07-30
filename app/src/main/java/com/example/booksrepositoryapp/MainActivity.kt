package com.example.booksrepositoryapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booksrepositoryapp.databinding.ActivityMainBinding
import com.example.booksrepositoryapp.ui.account_details.AccountDetailsFragment
import com.example.booksrepositoryapp.ui.book_category.BooksCategoryFragment
import com.example.booksrepositoryapp.ui.cart_screen.AddToCartFragment
import com.example.booksrepositoryapp.ui.landingpage.LandingPageFragment
import com.example.booksrepositoryapp.ui.utils.NavigationUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: NavigationUtil
    private val viewModel: MainActivityViewModel by viewModels()
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigator = NavigationUtil(supportFragmentManager)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoggedIn.collect { loggedIn ->
                    updateBottomNav(loggedIn)
                    if (!loggedIn) {
                        navigator.navigateAsRoot(LandingPageFragment())
                    } else if (
                        supportFragmentManager.findFragmentById(R.id.fragmentContainer) == null
                    ) {
                        navigator.navigateAsRoot(BooksCategoryFragment())
                    }
                }
            }
        }
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (!viewModel.isLoggedIn.value) {
                false
            } else {
                when (item.itemId) {
                    R.id.nav_home -> {
                        navigator.navigateAsRoot(BooksCategoryFragment())
                        true
                    }
                    R.id.nav_cart -> {
                        navigator.navigateAsRoot(AddToCartFragment())
                        true
                    }
                    R.id.nav_account -> {
                        navigator.navigateAsRoot(AccountDetailsFragment())
                        true
                    }
                    else -> false
                }
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                return@addCallback
            }
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Press back again to exit",
                    Toast.LENGTH_SHORT
                ).show()
                backPressedTime = System.currentTimeMillis()
            }
        }
    }

    fun updateSelectedBottomNav(itemId: Int) {
        binding.bottomNav.menu.findItem(itemId).isChecked = true
    }

    fun selectBottomNavItem(itemId: Int) {
        binding.bottomNav.selectedItemId = itemId
    }
    private fun updateBottomNav(isLoggedIn: Boolean) {
        binding.bottomNav.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
    }
}